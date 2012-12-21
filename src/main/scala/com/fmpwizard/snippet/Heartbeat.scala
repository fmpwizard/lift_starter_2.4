package com.fmpwizard.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{S, SHtml, DispatchSnippet}
import net.liftweb.http.js.{JE, JsCmd}
import net.liftweb.http.js.JsCmds.{Script, OnLoad}
import net.liftweb.json._

object Heartbeat extends DispatchSnippet{

  def dispatch = {
    case "render" => render
  }

  def jsonLoad =
    """{"param" : "%s"
      |
      |}""".format(S.param("id").openOr("-1")).stripMargin

  def render = {
    "*" #> Script(OnLoad(SHtml.jsonCall(JE.JsRaw(jsonLoad), imHere _).cmd))
  }

  def imHere(load: JValue): JsCmd = {
    println("we got %s" format load)
    implicit val format = DefaultFormats
    val user = (load \ "param").extract[String]
    UserCount ! UserBeat(user)
    Thread.sleep(2000)
    SHtml.ajaxInvoke(() => imHere(load)).cmd
  }
}
