package com.fmpwizard.lib

import net.liftweb.json._
import net.liftweb.common.Loggable

/**
 * Convert the Tweets into lift json JValues
 */
object TweetParser extends Loggable{
  def findTextAndName(s: String): JValue = {
    logger.debug(s)
    val json = parse(s)
    /**
     * We only want the username and the text they tweeted.
     */
    val jv = json \ "text" ++ json \ "user" \ "screen_name"
    jv
  }
}
