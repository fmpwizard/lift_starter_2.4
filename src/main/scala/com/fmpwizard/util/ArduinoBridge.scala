package com.fmpwizard
package util

import gnu.io.CommPortIdentifier
import gnu.io.SerialPort
import java.io.OutputStream
import java.util.Enumeration
import net.liftweb.actor.LiftActor
import net.liftweb.http.NamedCometListener
import net.liftweb.common.{Full, Logger}

/**
 * The liftActor responsible for communicating with the
 * Arduino board using a USB cable.
 */
object ArduinoBridge extends Logger with LiftActor{
  private var port: SerialPort = null
  private var cpi: CommPortIdentifier = null
  private var doorStatus: DoorStatus = CloseDoor

  def messageHandler = {
    case InitPort(port)      => init(port)
    case ArduinoMessage(msg) => writeToArduino(msg)
    case GetPort             => reply(port)
    case GetCPI              => reply(cpi)
    case GetDoorStatus       => reply(doorStatus: DoorStatus)
  }

  private def init( p: String ): Unit = {
    info("Calling init.")
    val enums: Enumeration[_] = CommPortIdentifier.getPortIdentifiers()

    /**
     * This doesn't look very scalaish at all, but I couldn't figure out what
     * else to do with Enums.
     */
    while (enums.hasMoreElements()) {
      cpi = enums.nextElement().asInstanceOf[CommPortIdentifier]
      if (p.equals(cpi.getName())) {
        info("cpi is %s".format(cpi))
        initPort()
        return
      }
    }
  }

  private def initPort() {
    try {
      port = cpi.open("ArduinoBridge", 1000).asInstanceOf[SerialPort]
      info("port is %s".format(port))
      if (port != null) {
        port.setSerialPortParams(9600,
          SerialPort.DATABITS_8,
          SerialPort.STOPBITS_1,
          SerialPort.PARITY_NONE);
      }
      info("Ready!");
    } catch {
      case e => e.printStackTrace()
    }
  }

  private def writeToArduino(value: Byte) {
    info("Sending a %s".format(value))
    try {
      val os: OutputStream = port.getOutputStream()

      os.write(0xff)
      os.write(value)
      os.flush()

      doorStatus = value match {
        case 1 => {
          /**
           * This is how you send a message to a CometActor with the name of "front-door", and the message
           * will reach Actors even if they are on different sessions.
           */
          NamedCometListener.getDispatchersFor(Full("front-door") ).foreach(
            dispatcher=> dispatcher.map(_ ! OpenDoor )
          )
          OpenDoor
        }
        case 2 => {
          NamedCometListener.getDispatchersFor(Full("front-door") ).foreach(
            dispatcher=> dispatcher.map(_ ! CloseDoor )
          )
          CloseDoor
        }
      }
      info("Door is %s".format(doorStatus))
      if (os != null) {
        os.close();
      }
    } catch {
      case e => {
        error("Cable not connected to Arduino!")
        e.printStackTrace()
        NamedCometListener.getDispatchersFor(Full("front-door") ).foreach(
          dispatcher=> dispatcher.map(_ ! NotConnected )
        )
      }
    }
  }
}
