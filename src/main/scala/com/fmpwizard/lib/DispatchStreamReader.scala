package com.fmpwizard.lib

import dispatch._
import com.yammer.metrics.scala.Instrumented

import Utils._
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.NamedCometListener
import com.fmpwizard.comet._
import java.util.concurrent.TimeUnit

object DispatchStreamReader extends Loggable with Instrumented{
  private val tps = metrics.meter("TPS", "Tweets", "DispatchStreamReader", TimeUnit.SECONDS)

  def go = {

    val s = :/("stream.twitter.com", 443) /
      "1/statuses/sample.json" <:<
      Map(
        "User-Agent" -> "Dispatch - Scala - Liftweb",
        "Authorization" -> buildHeader(),
        "Host" -> hostAndPort
      )  secure

    futures.DefaultFuture.future {
      Http(s >> {
        (stm,charset) => {
          import java.io._
          logger.info("start.")
          val reader: BufferedReader = new BufferedReader(new InputStreamReader(stm,charset))
          var line = reader.readLine()
          while (line != null) {
            tps.mark()
            Utils.updateStats(tps)
            line = reader.readLine()
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
