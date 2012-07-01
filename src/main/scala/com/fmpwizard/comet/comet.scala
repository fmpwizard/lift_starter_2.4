package com.fmpwizard

import net.liftweb.common.Box
import xml.NodeSeq

/**
 * These are all the messages our comet actor handle.
 */
package object comet {
  case class Tweet(message: Box[NodeSeq])
  case class MeanRate(time: Double)
  case class OneMinuteRate(time: Double)
  case class FiveMinuteRate(time: Double)
  case class NumOfTweets(t: Int)
}
