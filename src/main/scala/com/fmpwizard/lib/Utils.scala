package com.fmpwizard.lib

import com.yammer.metrics.scala.Meter
import net.liftweb.http.NamedCometListener
import net.liftweb.common.{Loggable, Full}
import com.fmpwizard.comet._
import com.twitter.util.Time
import com.twitter.joauth.{OAuthParams, StandardNormalizer, StandardSigner, OAuth1Params}
import org.apache.commons.codec.binary.Base64
import java.net.URLEncoder
import java.util
import net.liftweb.util.Props


object Utils extends Loggable{

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


  def updateStats(tps: Meter) = {
    if ((tps.count.toDouble / 100) % 2 == 0) {
      logger.info("Sending update at %s".format(tps.count))
      NamedCometListener.getDispatchersFor(Full("tweet")).foreach{
        actorM => actorM map {
          actor => {
            actor ! MeanRate( tps.meanRate.round )
            actor ! OneMinuteRate( tps.oneMinuteRate.round )
            actor ! FiveMinuteRate( tps.fiveMinuteRate.round )
            actor ! NumOfTweets( tps.count.toInt )
          }
        }
      }
      //clientFactory.close()
      //client.release()
    }
  }

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


}
