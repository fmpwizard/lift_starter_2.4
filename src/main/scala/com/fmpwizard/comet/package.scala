package com.fmpwizard

import net.liftweb.json.JsonAST.JValue

package object comet {
  case class Tweet(message: JValue)
  case class Duration(time: Long)
  case class Finished(t: Long)
}
