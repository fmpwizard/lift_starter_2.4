package com.fmpwizard

import net.liftweb.json.JsonAST.JValue
import net.liftweb.common.Box

package object comet {
  case class Tweet(message: Box[JValue])
  case class Duration(time: Long)
  case class Finished(t: Long)
}
