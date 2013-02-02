package com.fmpwizard.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import com.fmpwizard.gpio.Controller
import net.liftweb.common.{Box, Loggable}
import com.fmpwizard._
import com.pi4j.io.gpio.GpioPinDigitalOutput

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

    case StartRandom =>
      spiceUpLights()

  }

  private def updateWorld(m: PinAction) {
    pin = m
    logger.info("3- pin is: " + pin)
    updateListeners()
  }

  private def spiceUpLights() {
    import util.Random
    val pin = Random.shuffle( Controller.digitalOutPins ).headOption
    pin.foreach( p =>   this ! PinToggle( p ) )
    Thread.sleep(1000)
    this ! StartRandom
  }
}


