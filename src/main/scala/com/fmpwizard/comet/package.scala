package com.fmpwizard

import net.liftweb.json.JsonAST.JValue
import net.liftweb.common.Box

package object comet {
  case class Tweet(message: Box[JValue])
  case class MeanRate(time: Double)
  case class OneMinuteRate(time: Double)
  case class FiveMinuteRate(time: Double)
  case class NumOfTweets(t: Int)
}
