package com.fmpwizard.snippet

import net.liftweb.actor.LiftActor
import collection.mutable.Map
import org.joda.time.DateTime
import net.liftweb.util.Schedule
import net.liftweb.util.Helpers._

object UserCount extends LiftActor {

  private val users: Map[String, DateTime] = Map()
  private def cnt = users.size

  override def messageHandler = {
    case UserBeat(username) => {
      updateBeat(username)
      println(users)
      println(cnt)
    }
    case RemoveIdle => {
      users.collect{
        case (u, d) if d.isBefore(new DateTime().minusSeconds(3)) =>
          users -= u
          println("Deleted user, now back to %s users" format cnt)
      }
      Schedule.schedule(this, RemoveIdle, 3 seconds)
    }
    case ShowCount => {
      reply(cnt)
    }
  }

  private def updateBeat(user: String) {
    users -= user
    users += user -> new DateTime()
  }

}

sealed trait Actions
case class UserBeat(userName: String) extends Actions
case object RemoveIdle extends Actions
case object ShowCount extends Actions
