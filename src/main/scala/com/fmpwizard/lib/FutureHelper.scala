package com.fmpwizard.lib

import xml.NodeSeq

import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._
import net.liftweb.actor.{LAScheduler, LAFuture}
import net.liftweb.common._
import net.liftweb.http.{S, SHtml}


/**
 * This object could be part of Lift
 */
object FutureHelper extends Loggable{

  def laFuture2Lazy(
                     la:          LAFuture[JsCmd],
                     initLAF:     (LAFuture[JsCmd], String) => Unit,
                     idSelector:  String
                     )
  : NodeSeq = {

    LAScheduler.execute( () => initLAF( la, idSelector ) )
    Script(OnLoad( SHtml.ajaxInvoke( () => FutureIsHere( la ) ).exp.cmd ))
  }
}

/**
 * This could be part of Lift
 * @param la the LAFuture holding the JsCmd to update the UI
 */

case class FutureIsHere(la: LAFuture[JsCmd] ) extends JsCmd with Loggable {

  val updatePage = if (la.isSatisfied) {
    la.get
  } else {
    tryAgain()
  }

  private def tryAgain(): JsCmd = {
    val funcName: String = S.request.flatMap(_._params.toList.headOption.map(_._1)).openOr("")
    val retry = "setTimeout(function(){liftAjax.lift_ajaxHandler('%s=true', null, null, null)}, 3000)".format(funcName)

    JE.JsRaw(retry).cmd
  }

  override val toJsCmd = updatePage.toJsCmd
}
