package com.fmpwizard
package comet


import com.fmpwizard._
import gpio.Controller._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.json._
import js.{JsCmds, JE, JsCmd}
import com.pi4j.io.gpio.GpioPinDigitalOutput
import net.liftweb.common.Loggable


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
      "#pin *"            #> ("pin status: " + str2Pin("pin" + p.toString).isHigh) &
      "#pin [id]"         #> ("pin" + p.toString) &
      "#pinTitle *"       #> ("Pin " + p.toString + ": ") &
      "#toggle [onclick]" #> SHtml.jsonCall(JE.JsRaw("""{"pin" : "pin%s"}""".format(p)), togglePin _)
    }
  }

  /**
   * The messages that our comet can handle
   */
  override def lowPriority = {
    case PinUp(pin)     => pin.high()
    case PinDown(pin)   => pin.low()
    case PinToggle(pin) =>
      logger.info("1- pin status: " + pin.getState)
      partialUpdate(JE.JsRaw("""$("#%s").html("pin status: %s")""".format(pin.getName, pin.isHigh)).cmd)
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

  /**
   * Implicitly convert a jValue into a Pin
   * Used in togglePin
   */
  private[this] implicit def jv2Pin(in: JValue): GpioPinDigitalOutput = {
    implicit val foprmats = DefaultFormats
    logger.info("in is %s" format in)
    logger.info("pin is %s" format ((in \ "pin" ).extract[String]))
    (in \ "pin").extract[String] match {
      case "pin1" => pin1
      case "pin2" => pin2
      case "pin3" => pin3
      case "pin4" => pin4
      case "pin5" => pin5
      case "pin6" => pin6
      case "pin7" => pin7
      case "pin8" => pin8
      case "pin9" => pin9
      case "pin10" => pin10
      case "pin11" => pin11
      case "pin12" => pin12
      case "pin13" => pin13
      case "pin14" => pin14
      case "pin15" => pin15
      case "pin16" => pin16
      case _ => println("ee") ; pin1
    }
  }

  /**
   * We call this from render
   */
  private[this] def str2Pin(s: String): GpioPinDigitalOutput = {
    jv2Pin(JObject(List(JField("pin",JString(s)))))
  }

}
