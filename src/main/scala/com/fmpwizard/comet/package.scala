package com.fmpwizard

import com.pi4j.io.gpio.{GpioPinPwmOutput, GpioPinDigitalOutput}


/**
 * Messages to pass to our comet actors
 */
sealed trait PinAction
case class PinToggle(pin: GpioPinDigitalOutput) extends PinAction
case class PinPulse(pin: GpioPinDigitalOutput) extends PinAction
case class PinPWM( t:(GpioPinPwmOutput, Int)) extends PinAction
