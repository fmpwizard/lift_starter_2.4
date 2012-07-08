package com.fmpwizard
package actors

import net.liftweb.actor.LiftActor



object InboxActor extends LiftActor{

  private[this] var message = ""
  override def messageHandler ={
    case GetMessages    => reply(message)
    case AddMessage(s)  => message = s
    case RemoveMessage  => message = ""
  }

}
