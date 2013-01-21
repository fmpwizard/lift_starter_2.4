package com.fmpwizard.api

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.{Req, S, LiftResponse}
import net.liftweb.common.{Loggable, Box}
import net.liftweb.util.Helpers._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

object RaspberryPi extends RestHelper with Loggable {


  def listResources: () => Box[LiftResponse] = {
    val json = """ {"actions-uri" : "%s" }""".format(PiAST.GPIO)
    parse(json)

  }

  def listGPIO: () => Box[LiftResponse] = {
    val json = (1 to 16).foldLeft(JObject(Nil))(
      (jvalues , num) =>
        ("pin-uri" -> (PiAST.GPIO + "/%s" format num)) ~  jvalues
    )
    json
  }

  def gpioStatus(p: Int): () => Box[LiftResponse] = {
    ("status" -> "false") ~
    ("put-uri" -> (PiAST.GPIO + "/%s" format p))
  }

  def updatePin(pin: Int, json: JValue): () => Box[LiftResponse] = {
    logger.info("Pin is %s and json is %s".format(pin, json))
    val j: JValue = ("pin-status" -> "true")
    j
  }

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
  def unapply(id: String): Option[Int] = asInt(id)
}
