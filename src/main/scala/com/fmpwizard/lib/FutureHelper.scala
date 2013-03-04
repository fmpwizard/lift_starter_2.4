package com.fmpwizard.lib

import xml.NodeSeq

import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.SHtml
import net.liftweb.actor.{LAScheduler, LAFuture}
import net.liftweb.common.Loggable


object FutureHelper extends Loggable{

  def laFuture2Lazy(
                     la:          LAFuture[String],
                     initLAF:     LAFuture[String] => Unit,
                     resultFunc:  (LAFuture[String], String) => JsCmd,
                     idSelector:  String
                     )
  : NodeSeq = {

    LAScheduler.execute( () => initLAF( la ) )
    Script(OnLoad( SHtml.ajaxInvoke( () => resultFunc(la, idSelector) ).exp.cmd ))
  }


}
