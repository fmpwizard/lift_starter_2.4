package com.fmpwizard

import com.pi4j.io.gpio.GpioPinDigitalOutput


/**
 * Messages to pass to our comet actors
 */
case class PinUp(pin: GpioPinDigitalOutput)
case class PinDown(pin: GpioPinDigitalOutput)
case class PinToggle(pin: GpioPinDigitalOutput)
