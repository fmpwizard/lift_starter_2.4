package com.fmpwizard

import org.joda.time.DateTime

package object service{
  case class MessageRow(dateTime: DateTime, user: String, message: String, server: String)
}
