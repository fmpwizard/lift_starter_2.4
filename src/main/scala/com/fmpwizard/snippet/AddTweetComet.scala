package com.fmpwizard.snippet

import net.liftweb.http.NamedCometActorSnippet

/**
 * Adding this snippet to your html templates gives you a comet actor with the name
 * tweet and correspond to the class TweetWritter
 */
object AddTweetComet extends NamedCometActorSnippet {
  def name = "tweet"
  def cometClass = "TweetWritter"
}
