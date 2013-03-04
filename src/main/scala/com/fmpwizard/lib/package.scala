package com.fmpwizard.lib

import net.liftweb.http.js.JsCmds.CmdPair
import net.liftweb.http.js.{JE, JsCmd}

case class FutureIsHere(str: String, idSelector: String ) extends JsCmd {
  val replace = JE.JsRaw("""$("#%1$s").replaceWith('<span id="%1$s">Data: %2$s"</span>')"""
    .format(idSelector, str)
  ).cmd

  val updateCssClass = JE.JsRaw("""$("#%s").attr("class", "alert alert-success")""" format idSelector).cmd

  override val toJsCmd = CmdPair(replace, updateCssClass).toJsCmd
}
