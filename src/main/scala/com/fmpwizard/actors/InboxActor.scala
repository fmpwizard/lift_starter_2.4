package com.fmpwizard
package actors

import net.liftweb.actor.LiftActor
import net.liftweb.common._


object InboxActor extends LiftActor{


  private[this] var messages: List[(Box[String], Box[String])] = List((Full("diego"), Full("Hi")))
  override def messageHandler ={
    case GetMessages          => reply(messages)
    case AddMessages(u,m)     => messages = (u,m) :: messages
    case RemoveMessages(l)    => messages =  messages.filterNot( _ == l)
    case Message(u, m, d)     => println("We got a message from %s: %s, at %s".format(u, m, d))
  }

}
