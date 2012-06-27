package com.fmpwizard.lib

import net.liftweb.common.{Full, Loggable}
import net.liftweb.util.Props
import com.twitter.joauth.{StandardSigner, StandardNormalizer, OAuth1Params, OAuthParams}
import com.twitter.util.{Future, Time}
import com.twitter.conversions.time._
import java.util
import org.apache.commons.codec.binary.Base64
import java.net.URLEncoder
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpMethod, HttpVersion, DefaultHttpRequest}
import com.twitter.finagle.ServiceFactory
import com.twitter.finagle.stream.{Stream, StreamResponse}
import com.twitter.finagle.builder.ClientBuilder
import org.jboss.netty.util.CharsetUtil
import net.liftweb.http.NamedCometListener
import com.fmpwizard.comet._
import net.liftweb.common.Full
import com.twitter.joauth.OAuth1Params

/**
 * Finagle retrieving tweets from the Twitter stream api (the sample api)
 */
object StreamReader extends Loggable {

  val token = Props.get("twitter.token").getOrElse("")
  val consumerKey = Props.get("twitter.consumerKey").getOrElse("")
  val tokenSecret = Props.get("twitter.tokenSecret").getOrElse("")
  val consumerSecret = Props.get("twitter.consumerSecret").getOrElse("")

  val host = Props.get("twitter.host").getOrElse("")
  val port = Props.get("twitter.port").getOrElse("").toInt
  val hostAndPort = host + ":" + port
  val path = Props.get("twitter.path").getOrElse("")
  val creds: OAuthCredentials = OAuthCredentials(token, consumerKey, tokenSecret, consumerSecret)

  val signatureMethod = OAuthParams.HMAC_SHA1
  val scheme = "https"
  val oauthVersion = OAuthParams.ONE_DOT_OH

  val oauthStr = """OAuth oauth_consumer_key="%s",
                   oauth_nonce="%s",
                   oauth_signature="%s",
                   oauth_signature_method="%s",
                   oauth_timestamp="%s",
                   oauth_token="%s",
                   oauth_version="%s"
                 """.replaceAll(",\\s+", ", ")

  def buildHeader() = {
    val timestampSecs = Time.now.inSeconds
    val timestampStr = timestampSecs.toString
    val nonce = util.UUID.randomUUID().toString
    val oauthParams = OAuth1Params(creds.token, creds.consumerKey, nonce, timestampSecs, timestampStr, null, signatureMethod, oauthVersion)
    val normStr = StandardNormalizer(scheme, host, port, "GET", path, List(), oauthParams)
    val sig = StandardSigner
    val signed: String = new String(Base64.encodeBase64(sig.getBytes(normStr, creds.tokenSecret, creds.consumerSecret)))
    oauthStr.format(creds.consumerKey, nonce, URLEncoder.encode(signed, "UTF-8"), signatureMethod, timestampSecs, creds.token, oauthVersion).trim
  }

  val clientFactory: ServiceFactory[HttpRequest, StreamResponse] = ClientBuilder()
    .codec(Stream())
    .tls(host)
    .hosts(hostAndPort)
    .tcpConnectTimeout(1.second)
    .hostConnectionLimit(1)
    .buildFactory()

  val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path)
  request.setHeader("Authorization", buildHeader())
  request.setHeader("User-Agent", "Finagle 4.0.2 - Liftweb")
  request.setHeader("Host", hostAndPort)
  logger.debug("Sending request:\n%s".format(request))

  def go {


    val client = clientFactory.apply()()
    val streamResponse = client(request)
    streamResponse.onSuccess {
      streamResponse => {
        logger.debug("Good")
        var messageCount = 0 // Wait for 1000 messages then shut down.
        val startTime= Time.now.inMilliseconds
        streamResponse.messages foreach {
          buffer =>
            messageCount += 1
            NamedCometListener.getDispatchersFor(Full("tweet")).foreach{
              actorM => actorM map {
                actor => actor ! Tweet(TweetParser.findTextAndName(buffer.toString(CharsetUtil.UTF_8)))
              }
            }
            if (messageCount == 1000) {
              NamedCometListener.getDispatchersFor(Full("tweet")).foreach{
                actorM => actorM map {
                  actor => {
                    actor ! Duration( Time.now.inMilliseconds - startTime)
                    actor ! Finished( Time.now.inMilliseconds - startTime)
                  }
                }
              }
              client.release()
              //clientFactory.close()
            }
            // We return a Future indicating when we've completed processing the message.
            Future.Done
        }
      }
    }.onFailure(r => logger.error("We failed: %s".format(r)))

  }
}
