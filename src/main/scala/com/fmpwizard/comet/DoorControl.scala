package com.fmpwizard
package comet

import net.liftweb.util._
import Helpers._
import net.liftweb.http.js.JsCmds.SetHtml
import util._
import net.liftweb.http.{NamedCometActorTrait, SHtml}
import net.liftweb.common.{Full, Box, Logger}
import net.liftweb.http.js.{JsCmd, JE, JsCmds}

/**
 * The CometActor that keep you up-to-date.
 */
class DoorControl extends Logger with NamedCometActorTrait{

  private var status: Box[DoorStatus] = (ArduinoBridge !! GetDoorStatus).asInstanceOf[Box[DoorStatus]]

  def render = {
    "#status *+" #> (status.openOr("Closed") + ".") &
    "#doorknob [onclick]" #> SHtml.ajaxInvoke(() => openOrClose())
  }

  override def lowPriority ={
    case CloseDoor => {
      status = Full(CloseDoor)
      partialUpdate(SetHtml("status", <span>The door is closed</span>))
      partialUpdate(JE.JsRaw("""$("#doorknob").text("Open")""").cmd)
    }
    case OpenDoor   => {
      status = Full(OpenDoor)
      partialUpdate(SetHtml("status", <span>The door is open</span>))
      partialUpdate(JE.JsRaw("""$("#doorknob").text("Close")""").cmd)
    }
    case NotConnected => {
      partialUpdate(SetHtml("status", <span>The door is not connected, check the usb cable.</span>))
    }

  }

  /**
   * Decide if we need to close or open the door
   */
  private def openOrClose(): JsCmd = {
    status map {
      _ match {
        case CloseDoor | NotConnected => {
          ArduinoBridge ! ArduinoMessage(1)
        }
        case OpenDoor => {
          ArduinoBridge ! ArduinoMessage(2)
        }
      }
    }
    JsCmds.Noop
  }
}
