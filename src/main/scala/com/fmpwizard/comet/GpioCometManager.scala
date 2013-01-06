package com.fmpwizard.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import com.fmpwizard.PinToggle
import com.fmpwizard.gpio.Controller

object GpioCometManager extends LiftActor with ListenerManager {
  var pin: PinToggle = PinToggle(Controller.pin1)

  def createUpdate = pin
  override def lowPriority = {
    case m@PinToggle(currentPi) =>
      pin = m
      updateListeners()
  }
}


