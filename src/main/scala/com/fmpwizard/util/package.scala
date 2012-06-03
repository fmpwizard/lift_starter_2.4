package com.fmpwizard

/**
 * Messages used on our CoemtActor and LiftActor
 */
package  object  util {

  case class InitPort(port: String)
  case class ArduinoMessage(msg: Byte)
  case object GetPort
  case object GetCPI
  case object GetDoorStatus

  sealed trait DoorStatus
  case object CloseDoor     extends DoorStatus { override def toString = "Closed" }
  case object OpenDoor      extends DoorStatus { override def toString = "Open" }
  case object NotConnected  extends DoorStatus { override def toString = "Not connected" }

}
