package com.fmpwizard.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import com.fmpwizard.gpio.Controller
import net.liftweb.common.Loggable
import com.fmpwizard._
import net.liftweb.util.Schedule
import net.liftweb.util.Helpers._

/**
 * This LiftActor tells all comet actors in this jvm when a pin state has changed.
 */
object GpioCometManager extends LiftActor with ListenerManager with Loggable {

  var pin: PinAction = PinToggle(Controller.pin1)

  def createUpdate = pin

  private var runShow_? = true

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
      startLightShow()

    case StopRandom =>
      stopLightShow()

    case InitLightsCron =>
      spiceUpLights()

  }

  private def updateWorld(m: PinAction) {
    pin = m
    logger.info("3- pin is: " + pin)
    updateListeners()
  }

  private def spiceUpLights() {
    import util.Random
    import scala.language.postfixOps
    if ( runShow_? ) {
      val pin = Random.shuffle( Controller.digitalOutPins ).headOption
      pin.foreach( p =>   this ! PinToggle( p ) )
    }
    Schedule.schedule(this, InitLightsCron, 1 second )
  }

  private def stopLightShow() {
    runShow_? = false
  }

  private def startLightShow() {
    runShow_? = true
  }
}


