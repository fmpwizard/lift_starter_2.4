package com.fmpwizard.snippet

import net.liftweb.http.SHtml
import net.liftweb.http.js.{JE, JsCmds, JsCmd}
import net.liftweb.util.Helpers._
import net.liftweb.common.Loggable

class Sample extends Loggable {
  def render = {
    var one = ""
    "#one" #> SHtml.ajaxText(one, string => {
      one = string
      logger.info("You just entered '%s' on the browser" format string)
      JE.JsRaw("""alert("You just entered '%s'")""".format(string)).cmd
    }) &
    "#button2 [onclick]" #> SHtml.ajaxCall( JE.JsRaw("""$("#one").val()"""),  handleClick _ )
  }

  def handleClick(s: String): JsCmd = {
    logger.info("We got '%s' from the server" format s)
    JE.JsRaw("""alert("Button clicked, value was: '%s'")""".format(s)).cmd
  }


}
