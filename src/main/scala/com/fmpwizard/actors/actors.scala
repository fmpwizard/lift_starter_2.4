package com.fmpwizard

package object actors {
  case object GetMessages
  case object RemoveMessage
  case class AddMessage(m: String)

}
