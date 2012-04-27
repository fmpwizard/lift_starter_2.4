package com.fmpwizard.snippet

import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import net.liftweb.util.Helpers._

class Sample{
  def render = {
    "#button [onclick]" #> SHtml.ajaxInvoke{ () =>
      JsCmds.Alert("Got it!")
    }._2.cmd &
    "#button2 [onclick]" #> (SHtml.ajaxInvoke{ () =>
      JsCmds.Alert("Got it!")
    }._2.cmd.toJsCmd + "return false;")
  }
}
