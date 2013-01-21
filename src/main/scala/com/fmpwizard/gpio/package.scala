package com.fmpwizard

import gpio.Controller._
import com.pi4j.io.gpio.GpioPinDigitalOutput
import net.liftweb.common._

package object gpiolib extends Loggable {
  def str2Pin(s: String): Box[GpioPinDigitalOutput] = {
    s match {
      case "pin1" => Full(pin1)
      case "pin2" => Full(pin2)
      case "pin3" => Full(pin3)
      case "pin4" => Full(pin4)
      case "pin5" => Full(pin5)
      case "pin6" => Full(pin6)
      case "pin7" => Full(pin7)
      case "pin8" => Full(pin8)
      case "pin9" => Full(pin9)
      case "pin10" => Full(pin10)
      case "pin11" => Full(pin11)
      case "pin12" => Full(pin12)
      case "pin13" => Full(pin13)
      case "pin14" => Full(pin14)
      case "pin15" => Full(pin15)
      case "pin16" => Full(pin16)
      case x =>
        logger.error("Failed to convert %s into a pin" format x)
        Failure("Failed to convert %s into a pin" format x)
    }

  }
}
