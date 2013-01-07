package com.fmpwizard.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import com.fmpwizard.PinToggle
import com.fmpwizard.gpio.Controller
import net.liftweb.common.Loggable

/**
 * This LiftActor tells all comet actors in this jvm when a pin state has changed.
 */
object GpioCometManager extends LiftActor with ListenerManager with Loggable {

  var pin: PinToggle = PinToggle(Controller.pin1)

  def createUpdate = pin
  override def lowPriority = {
    case m@PinToggle(currentPi) =>
      currentPi.toggle()
      pin = m
      logger.info("3- pin is: " + pin)
      updateListeners()
      logger.info("4- pin is: " + pin)
  }
}


