package com.fmpwizard.comet

import net.liftweb.http.NamedCometActorTrait
import net.liftweb.http.js.jquery.JqJsCmds.PrependHtml
import xml.{NodeSeq, Text}
import net.liftweb.http.js.JsCmds.{Run, Replace, SetHtml}
import com.twitter.util.Time
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.json
import org.joda.time.DateTime

/**
 * Our comet actor that sends the messages to the browser.
 * We are extending the NamedCometActorTrait trait which
 * allows an easy way to send messages to a comet actor from outside the current
 * session.
 */
class TweetWritter extends NamedCometActorTrait{

  override def lowPriority: PartialFunction[Any, Unit] = {
    /**
     * If you want the tweets to stay on the page, you can use PrependHtml instead of SetHtml
     */
    //case Tweet(t)           => partialUpdate(PrependHtml("msg", <span>{t.openOr("")}</span><hr></hr>))
    case Tweet(t)           => partialUpdate(SetHtml("msg", <span>{t.openOr("")}</span><hr></hr>))
    case MeanRate(d)        => partialUpdate(SetHtml("meanrate", Text(d.toString)))
    case OneMinuteRate(d)   => partialUpdate(SetHtml("oneminuterate", Text(d.toString)))
    case FiveMinuteRate(d)  => partialUpdate(SetHtml("fiveminuterate", Text(d.toString)))
    case NumOfTweets(t)     => {
      partialUpdate(SetHtml("numoftweets", Text(t.toString)))
      partialUpdate(Run("""$('title').replaceWith('<title>"""+ t.toString+  """ total tweets</title>');""" ))
    }
  }

  /**
   * We add the start date and time to the page.
   * This value does not need to be updated on every comet update.
   */
  def render ={
    "#boottime" #> bootstrap.liftweb.Stats.startedAt
  }
}
