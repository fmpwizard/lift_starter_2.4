package com.fmpwizard.lib

import net.liftweb.http.js.JsCmds.CmdPair
import net.liftweb.http.js.{JsCmds, JE, JsCmd}
import net.liftweb.common.Loggable
import net.liftweb.http.S
import net.liftweb.actor.LAFuture

case class FutureIsHere(la: LAFuture[String], idSelector: String ) extends JsCmd with Loggable {

  val updateCssClass = JE.JsRaw("""$("#%s").attr("class", "alert alert-success")""" format idSelector).cmd

  val  replace = if (la.isSatisfied) {
    updateElement()
  } else {
    tryAgain()
  }


  private def updateElement(): JsCmd = {
    val inner = JE.JsRaw("""$("#%1$s").replaceWith('<span id="%1$s">Data: %2$s"</span>')"""
      .format(idSelector, la.get)
    ).cmd
    CmdPair(inner, updateCssClass)
  }

  private def tryAgain(): JsCmd = {
    val funcName: String = S.request.flatMap(_._params.toList.headOption.map(_._1)).openOr("")
    val retry = "setTimeout(function(){liftAjax.lift_ajaxHandler('%s=true', null, null, null)}, 3000)"
    JE.JsRaw(retry.format(funcName)).cmd
  }

  override val toJsCmd = replace.toJsCmd
}
