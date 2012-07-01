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

  /**
   * Using Code Hale's metrics library
   */
  private val tps = metrics.meter("TPS", "Tweets", "StreamReader", TimeUnit.SECONDS)

  /**
   * You init a clientFactory only once and use it several times across your application
   */
  val clientFactory: ServiceFactory[HttpRequest, StreamResponse] = ClientBuilder()
    .codec(Stream())
    .tls(host)
    .hosts(hostAndPort)
    .tcpConnectTimeout(1.second)
    .hostConnectionLimit(1)
    .buildFactory()


  /**
   * The method we call from Boot, yo ucan also call a method like this
   * from a snippet if you want to just retrieve x number opf tweets.
   * Just add a logic to stop at x tweets.
   */
  def go {

    /**
     * Setup the header. We are using the oAuth method.
     */
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path)
    request.setHeader("Authorization", buildHeader)
    request.setHeader("User-Agent", "Finagle 4.0.2 - Liftweb")
    request.setHeader("Host", hostAndPort)
    logger.debug("Sending request:\n%s".format(request))

    /**
     * We init the client here.
     */

    val client = clientFactory.apply()()
    val streamResponse = client(request)

    /**
     * What to do if the Future from the previous line returned ok
     */
    streamResponse.onSuccess {
      streamResponse => {
        logger.debug("Good")

        /**
         * Walk through the tweets.
         */
        streamResponse.messages foreach {
          buffer => {
            /**
             * Increase the counter from Metrics
             */
            tps.mark()

            /**
             * Find all the comet actors under the name "tweet" and send them our tweet.
             * This method finds comet actors from *any* session.
             */
            NamedCometListener.getDispatchersFor(Full("tweet")).foreach{
              actorM => actorM map {
                actor => actor ! Tweet(TweetParser.findTextAndName(buffer.toString(CharsetUtil.UTF_8)))
              }
            }
            //Send updated stats to the browser every 200 tweets
            Utils.updateStats(tps)
            //I wanted to see which steps are done at what time, this is why I have all these debug logs.
            logger.debug("1")
            // We return a Future indicating when we've completed processing the message.
            Future.Done
            logger.debug("2")
          }
          logger.debug("3")
        }

        /**
         * We do not release them because we never stop unless the jvm restarted.
         * normally you want to client.release once you are done getting data from the net.
         */
        //client.release()
        logger.debug("4")
      }
      logger.debug("5")

      /**
       * What to do if something went wrong stablishing the first connection.
       */
    }.onFailure(r => logger.error("We failed: %s".format(r)))
    logger.debug("6")
    //clientFactory.close()
  }
  logger.debug("7")
}
