package com.fmpwizard

import com.pi4j.io.gpio.{GpioPinPwmOutput, GpioPinDigitalOutput}
import com.pi4j.wiringpi.Gpio


/**
 * Messages to pass to our comet actors
 */
sealed trait PinAction
case class PinToggle(pin: GpioPinDigitalOutput) extends PinAction
case class PinUp(pin: GpioPinDigitalOutput)     extends PinAction
case class PinDown(pin: GpioPinDigitalOutput)   extends PinAction
case class PinPulse(pin: GpioPinDigitalOutput)  extends PinAction
case class PinPWM( t:(GpioPinPwmOutput, Int))   extends PinAction

case object StartRandom                         extends PinAction
case object StopRandom                          extends PinAction
