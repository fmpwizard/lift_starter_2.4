package com.fmpwizard
package comet


import com.fmpwizard._
import com.fmpwizard.gpio.Controller._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.json._
import js.{JsCmds, JE, JsCmd}
import com.pi4j.io.gpio.{GpioPinPwmOutput, GpioPinDigitalOutput}
import net.liftweb.common.Loggable
import scala.language.implicitConversions
import com.fmpwizard.PinDown
import com.fmpwizard.PinPWM
import com.fmpwizard.PinToggle
import com.fmpwizard.PinUp
import com.fmpwizard.PinPulse


class Gpio extends CometActor with Loggable with CometListener {

  /**
   * We want all our comets to get updates pin statuses
   */
  def registerWith = GpioCometManager

  /**
   * Displaying all pins, in a nice list
   * Also create a toggle button (all run using ajax)
   */
  def render = {
    "#pinRow *" #> (1 to 16).toList.map{ p =>
      "#pin *"            #> pinStatus("pin" + p)
      "#pin [id]"         #> ("pin" + p.toString) &
      "#pinTitle *"       #> ("Pin " + p.toString + ": ") &
      "#toggle [onclick]" #> SHtml.jsonCall(JE.JsRaw("""{"pin" : "pin%s"}""".format(p)), togglePin _)
    } &
    "#start-show [onclick]" #> SHtml.ajaxInvoke(() => GpioCometManager ! StartRandom) &
    "#stop-show [onclick]"  #> SHtml.ajaxInvoke(() => GpioCometManager ! StopRandom)
  }

  /**
   * The messages that our comet can handle
   */
  override def lowPriority = {
    case PinToggle(pin) =>
      logger.info("1- pin status: " + pin.getState)
      partialUpdate(updateLEDStatus(pin))
    case PinUp(pin) =>
      logger.info("1- pin status: " + pin.getState)
      partialUpdate(updateLEDStatus(pin))
    case PinDown(pin) =>
      logger.info("1- pin status: " + pin.getState)
      partialUpdate(updateLEDStatus(pin))
    case PinPWM((p, t))      =>
      partialUpdate(JE.JsRaw("""$("#%s").html("pin turn: %s")""".format(p.getName, t)).cmd)
  }

  /**
   * This gets called by pressing the toggle button next to each pin
   */
  private[this] def togglePin(pin: JValue): JsCmd = {
    GpioCometManager ! PinToggle(pin)
    val p: GpioPinDigitalOutput = pin
    logger.info("toogle pin " + p)
    JsCmds.Noop
  }

  private[this] def pulsePin(pin: JValue): JsCmd = {
    GpioCometManager ! PinPulse(pin)
    val p: GpioPinDigitalOutput = pin
    logger.info("pulse pin " + p)
    JsCmds.Noop
  }


  /**
   * Implicitly convert a jValue into a Pin
   * Used in togglePin
   */
  private[this] implicit def jv2Pin(in: JValue): GpioPinDigitalOutput = {
    implicit val foprmats = DefaultFormats
    logger.info("in is %s" format in)
    logger.info("pin is %s" format ((in \ "pin" ).extract[String]))
    val pinString = (in \ "pin").extract[String]
    gpiolib.str2Pin(pinString).openOr(pin2)
  }

  /**
   * We call this from render
   */
  private[this] implicit def str2Pin(s: String): GpioPinDigitalOutput = {
    jv2Pin(JObject(List(JField("pin",JString(s)))))
  }

  /**
   * Update the status and change the css class based on the PIN status
   */
  private def updateLEDStatus(p: GpioPinDigitalOutput): JsCmd = {
    val cssClass = if( p.isHigh ) {
      JE.JsRaw("""$("#%s").attr("class", "label label-success")""" format p.getName ).cmd
    } else {
      JE.JsRaw("""$("#%s").attr("class", "label label-important")""" format p.getName ).cmd
    }
    JE.JsRaw("""$("#%s").html("%s")""".format( p.getName, pinStatus(p) ) ).cmd &
    cssClass
  }

  private def pinStatus(p: GpioPinDigitalOutput): String = {
    if( p.isHigh ) {
      "pin status: On"
    } else {
      "pin status: Off"
    }
  }


}
