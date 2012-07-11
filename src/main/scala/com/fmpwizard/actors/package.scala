package com.fmpwizard

import net.liftweb.common.Box
import org.joda.time.DateTime

package object actors {
  case object GetMessages
  case class RemoveMessages(l: List[(Box[String], Box[String])])
  case class AddMessages(user: Box[String], m: Box[String])

  case class Message(user: String, msg: String, dateTime: DateTime)

  //https://fmpwizard.iriscouch.com:6984/_utils/database.html?chatserver
}
