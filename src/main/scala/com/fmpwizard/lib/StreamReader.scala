package com.fmpwizard.lib

import net.liftweb.common.Loggable
import com.twitter.util.Future
import com.twitter.conversions.time._
import java.util
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpMethod, HttpVersion, DefaultHttpRequest}
import com.twitter.finagle.ServiceFactory
import com.twitter.finagle.stream.{Stream, StreamResponse}
import com.twitter.finagle.builder.ClientBuilder
import org.jboss.netty.util.CharsetUtil
import net.liftweb.http.NamedCometListener
import com.fmpwizard.comet._
import net.liftweb.common.Full
import com.yammer.metrics.scala.Instrumented
import util.concurrent.TimeUnit

import Utils._

/**
 * Finagle retrieving tweets from the Twitter stream api (the sample api)
 */
object StreamReader extends Loggable with Instrumented{

  private val tps = metrics.meter("TPS", "Tweets", "StreamReader", TimeUnit.SECONDS)

  val clientFactory: ServiceFactory[HttpRequest, StreamResponse] = ClientBuilder()
    .codec(Stream())
    .tls(host)
    .hosts(hostAndPort)
    .tcpConnectTimeout(1.second)
    .hostConnectionLimit(1)
    .buildFactory()


  def go {

    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path)
    request.setHeader("Authorization", buildHeader())
    request.setHeader("User-Agent", "Finagle 4.0.2 - Liftweb")
    request.setHeader("Host", hostAndPort)
    logger.debug("Sending request:\n%s".format(request))

    val client = clientFactory.apply()()
    val streamResponse = client(request)
    streamResponse.onSuccess {
      streamResponse => {
        logger.debug("Good")
        streamResponse.messages foreach {
          buffer => {
            tps.mark()
            NamedCometListener.getDispatchersFor(Full("tweet")).foreach{
              actorM => actorM map {
                actor => actor ! Tweet(TweetParser.findTextAndName(buffer.toString(CharsetUtil.UTF_8)))
              }
            }
            //Send updated stats to the browser every 100 tweets
            Utils.updateStats(tps)
            // We return a Future indicating when we've completed processing the message.
            logger.debug("1")
            Future.Done
            logger.debug("2")
          }
          logger.debug("3")
        }
        //client.release()  //We do not release them because we never stop unless the jvm restarted.
        logger.debug("4")
      }
      logger.debug("5")
    }.onFailure(r => logger.error("We failed: %s".format(r)))
    logger.debug("6")
    //clientFactory.close()
  }
  logger.debug("7")
}
