package com.fmpwizard.lib

import net.liftweb.json._
import net.liftweb.common.{Box, Loggable}
import net.liftweb.util.Helpers._

/**
 * Convert the Tweets into lift json JValues
 */
object TweetParser extends Loggable{
  def findTextAndName(s: String): Box[JValue] = {
    val json = tryo{ t: Throwable =>  logger.info("Tried to parse %s".format(s))}( parse(s) )
    /**
     * We only want the username and the text they tweeted.
     */
    val jv = json map{
      x => x \ "text" ++ x \ "user" \ "screen_name"
    }
    jv
  }
}
