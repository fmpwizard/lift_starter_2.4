package com.fmpwizard.comet

import net.liftweb.http.NamedCometActorTrait
import net.liftweb.http.js.jquery.JqJsCmds.PrependHtml
import xml.{NodeSeq, Text}
import net.liftweb.http.js.JsCmds.SetHtml
import com.twitter.util.Time
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.json


class TweetWritter extends NamedCometActorTrait{

  override def lowPriority: PartialFunction[Any, Unit] = {
    case Tweet(t) => partialUpdate(PrependHtml("msg", <span>{formatTweet(t.openOr(""))}</span><hr></hr>))
    case MeanRate(d) => partialUpdate(SetHtml("meanrate", Text(d.toString)))
    case OneMinuteRate(d) => partialUpdate(SetHtml("oneminuterate", Text(d.toString)))
    case FiveMinuteRate(d) => partialUpdate(SetHtml("fiveminuterate", Text(d.toString)))
    case NumOfTweets(t) => partialUpdate(SetHtml("numoftweets", Text(t.toString)))
  }

  def render ={
    "#lala" #> ""
  }

  private def formatTweet(t: JValue): NodeSeq ={
    t match {
      case JNothing => Text("Empty tweet")
      case x        => Text(compact(json.render(x)))
    }
  }

}
