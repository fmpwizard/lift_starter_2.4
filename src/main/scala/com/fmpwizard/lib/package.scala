package com.fmpwizard


package object lib {
  case class OAuthCredentials(token: String, consumerKey: String, tokenSecret: String, consumerSecret: String)
}
