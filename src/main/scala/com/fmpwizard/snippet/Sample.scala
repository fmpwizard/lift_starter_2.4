package com.fmpwizard.snippet

import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JsCmd
import net.liftweb.util.Helpers._
import net.liftweb.common.Empty
import com.fmpwizard.comet.{cometLocale, Data}
import java.util.Locale

object Sample{

  val localesLookup: Map[String, Locale] =
    Map(
      Locale.GERMAN.toString -> Locale.GERMAN,
      Locale.US.toString -> Locale.US,
      Locale.FRANCE.toString -> Locale.FRANCE
    )
  val locales: Seq[String] = Seq(
    Locale.GERMAN.toString,
    Locale.US.toString,
    Locale.FRANCE.toString
  )

  /**
   * A button that sends an ajax message to the server
   */
  def render = {
    "#button2 [onclick]" #> SHtml.ajaxInvoke(() => handleClick("login"))
  }

  /**
   * Sends a message to a comet of class SampleComet, with no name
   */
  def handleClick(s: String): JsCmd = {
    for {
      sess  <- S.session
    } sess.sendCometActorMessage("SampleComet", Empty, Data(s))
  }

  /**
   * Change the locale of a comet actor ( by changing a session variable)
   */
  def changeLocale = {
    "#localeSelect" #> SHtml.ajaxSelectElem(locales, Empty){
      selected =>
        localesLookup.get(selected).foreach(cometLocale.set(_))
        handleClick("login")
    }
  }


}
