package com.fmpwizard.snippet

import net.liftweb.http.SHtml
import net.liftweb.http.js.{JE, JsCmds, JsCmd}
import net.liftweb.util.Helpers._

class Sample{
  def render = {
    "#button2 [onclick]" #> SHtml.ajaxCall( JE.JsRaw("this.name"),  handleClick _ )
  }

  def handleClick(s: String): JsCmd = {
    JE.JsRaw("""alert("Button clicked, value was: %s")""".format(s)).cmd
  }


}
