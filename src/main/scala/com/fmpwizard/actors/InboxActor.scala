package com.fmpwizard
package actors

import net.liftweb.actor.LiftActor
import net.liftweb.common._
import service._
import net.liftweb.http.NamedCometListener
import net.liftweb.util.Schedule
import net.liftweb.util.Helpers._
import actors._
import net.liftweb.common.Full

object InboxActor extends LiftActor with Logger{


  private[this] var since: BigInt = 0
  override def messageHandler ={
    case m @ Message(_,_,_)       => CentralChatServer.sendToCouchDB(m)
    case m @ MessageRow(_,_,_,_)  => {
      debug("Got a MessageRow, sending up to comet dispatch")
      NamedCometListener.getDispatchersFor(Full("chat")).foreach(actor => actor.map(_ ! m))
    }
    case Since(s)                 => {
      if (s > 0) since = s
      Schedule.schedule(() => CentralChatServer.readChangesFeed(since),  seconds(1))
    }
  }



}
