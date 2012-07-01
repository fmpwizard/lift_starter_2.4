package com.fmpwizard.lib

import net.liftweb.json._
import net.liftweb.common.{Box, Loggable}
import net.liftweb.util.Helpers._
import xml.{Text, NodeSeq}
import net.liftweb.json

/**
 * Convert the Tweets into lift json JValues
 */
object TweetParser extends Loggable{
  def findTextAndName(s: String): Box[NodeSeq] = {
    /**
     * If we have an authentication problem, we get an html page, so this is why
     * we use tryo and log the page content in that case.
     */
    val json = tryo{ t: Throwable =>  logger.info("Tried to parse %s".format(s))}( parse(s) )
    /**
     * We only want the username and the text they tweeted.
     */
    val jv = json map{
      x => x \ "text" ++ x \ "user" \ "screen_name"
    }
    jv.map(formatTweet(_))
  }

  /**
   * Some tweets don't have a text field, so we handle those here.
   */
  private def formatTweet(t: JValue): NodeSeq ={
    t match {
      case JNothing => Text("This tweet was deleted.")
      case x        => Text(compact(json.render(x)))
    }
  }

}
