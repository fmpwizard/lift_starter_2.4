package com.fmpwizard.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import com.fmpwizard._
import com.fmpwizard.gpio.Controller
import net.liftweb.common.Loggable
import com.pi4j.io.gpio.GpioPinPwmOutput
import com.fmpwizard.PinToggle

/**
 * This LiftActor tells all comet actors in this jvm when a pin state has changed.
 */
object GpioCometManager extends LiftActor with ListenerManager with Loggable {

  var pin: PinAction = PinToggle(Controller.pin1)

  def createUpdate = pin

  override def lowPriority = {

    case m@ PinToggle(currentPin) =>
      currentPin.toggle()
      updateWorld(m)
    case up@ PinUp(currentPin) =>
      currentPin.high()
      updateWorld(up)
    case low@ PinDown(currentPin) =>
      currentPin.low()
      updateWorld(low)
  }

  private def updateWorld(m: PinAction) {
    pin = m
    logger.info("3- pin is: " + pin)
    updateListeners()
  }
}


