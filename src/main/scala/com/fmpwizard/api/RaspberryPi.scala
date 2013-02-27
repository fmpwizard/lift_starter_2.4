package com.fmpwizard
package api

import net.liftweb.http.rest.RestHelper
import net.liftweb.http._
import net.liftweb.common.{Full, Loggable, Box}
import net.liftweb.util.Helpers._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.pi4j.io.gpio.GpioPinDigitalOutput
import comet.GpioCometManager
import com.fmpwizard.PinUp
import com.fmpwizard.PinDown
import com.fmpwizard.PinUp

object RaspberryPi extends Loggable with PiHelper {

  serve {
    case "api" :: "raspberrypi" :: Nil                      Get _ => listResources
    case "api" :: "raspberrypi" :: "gpio" :: Nil            Get _ => listGPIO
    case "api" :: "raspberrypi" :: "gpio" :: Pin(id) :: Nil Get _ => gpioStatus(id)
  }

  serve {
    case "api" :: "raspberrypi" :: "gpio" :: Pin(id) :: Nil JsonPut payload => updatePin(id, payload._1)
  }

}


object PiAST {
  val RESTBASE = "/api/raspberrypi"
  val GPIO = S.hostAndPath + PiAST.RESTBASE + "/gpio"
}

object Pin {
  def unapply(id: String): Option[GpioPinDigitalOutput] =
    gpiolib.str2Pin(id)
}

trait PiHelper extends RestHelper with Loggable {
  def listResources: () => Box[LiftResponse] = {
    val json = """ {"actions-uri" : "%s" }""".format(PiAST.GPIO)
    parse(json)

  }

  def listGPIO: () => Box[LiftResponse] = {
    import scala.collection.JavaConversions._
    import com.fmpwizard.gpio.Controller.gpio

    val json = (gpio.getProvisionedPins.map(_.getName)).foldLeft(JObject(Nil))(
      (jvalues , num) =>
        JObject( List( JField( "pin-uri", ( PiAST.GPIO + "/%s" format num ) ) ) ) ~  jvalues
    )
    json
  }

  def gpioStatus(p: GpioPinDigitalOutput): () => Box[LiftResponse] = {
    ("status" -> "%s".format(p.isHigh)) ~
      ("put-uri" -> (PiAST.GPIO + "/%s" format p.getName))
  }

  def updatePin(pin: GpioPinDigitalOutput, json: JValue): () => Box[LiftResponse] = {
    logger.info("Pin is %s and json is %s".format(pin, json))
    val status = (json  \ "status").extractOpt[Boolean]

    status.map{st =>
      if (st) {
        GpioCometManager ! PinUp(pin)
      } else {
        GpioCometManager ! PinDown(pin)
      }
    }
    () => Full(OkResponse.apply())
  }
}
