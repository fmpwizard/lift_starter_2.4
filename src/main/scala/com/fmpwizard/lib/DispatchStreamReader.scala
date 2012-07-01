package com.fmpwizard.lib

import dispatch._
import com.yammer.metrics.scala.Instrumented

import Utils._
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.NamedCometListener
import com.fmpwizard.comet._
import java.util.concurrent.TimeUnit

/**
 * Using Dispatch to retrieve tweets from the
 * Twitter stream sample api
 */
object DispatchStreamReader extends Loggable with Instrumented{
  private val tps = metrics.meter("TPS", "Tweets", "DispatchStreamReader", TimeUnit.SECONDS)

  /**
   * Our main method. Yo ucan call this from Boot or from a snippet.
   */
  def go = {

    val s = :/("stream.twitter.com", 443) /  "1/statuses/sample.json" <:<
      Map("User-Agent" -> "Dispatch - Scala - Liftweb", "Authorization" -> buildHeader, "Host" -> hostAndPort) secure

    futures.DefaultFuture.future {
      Http(s >> {
        (stm,charset) => {
          import java.io._
          logger.info("start.")
          val reader: BufferedReader = new BufferedReader(new InputStreamReader(stm,charset))
          var line = reader.readLine()
          while (line != null) {
            tps.mark() // increase the metrics counter
            Utils.updateStats(tps) //Send updated stats to the browser every 20 tweets.
            line = reader.readLine()

            /**
             * Find all the comet actors that have the name "tweet" and send them the current tweet to display it on the
             * browser.
             * This method find comet actors from all sessions on this jvm
             */
            NamedCometListener.getDispatchersFor(Full("tweet")).foreach{
              actor => actor map {_ ! Tweet(TweetParser.findTextAndName(line))}
            }
          }
          logger.info("done.")
          stm.close
        }
      })
    }
  }
}
