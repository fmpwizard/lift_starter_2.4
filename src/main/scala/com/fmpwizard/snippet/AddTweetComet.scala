package com.fmpwizard.snippet

import net.liftweb.http.NamedCometActorSnippet

object AddTweetComet extends NamedCometActorSnippet {
  def name = "tweet"
  def cometClass = "TweetWritter"
}
