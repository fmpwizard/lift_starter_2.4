package com.fmpwizard
package util

import org.specs2.mutable._
import net.liftweb.common.Full


class ArduinoBridgeSpec extends Specification {

  "The 'ArduinoBridge' axtor" should {
    "find this port" in {
      ArduinoBridge ! InitPort("tty.usbmodemfa131")
      ArduinoBridge !! GetPort must_== Full("/dev/tty.usbmodemfa131")
    }
  }
}
