package com.fmpwizard
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.actor._
import net.liftweb.common.Loggable
import xml.NodeSeq
import net.liftweb.http.js.JsCmd
import lib.FutureIsHere
import lib.MyAppLogic._
import net.liftweb.util.CanBind
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.SHtml

class Sample extends Loggable {

  val f1: LAFuture[JsCmd] = new LAFuture()
  val f2: LAFuture[JsCmd] = new LAFuture()

  LAScheduler.execute( () => querySlowService1( f1, "future1" ) )
  LAScheduler.execute( () => querySlowService2( f2, "future2" ) )

  implicit def laFutureJsCmdTransform: CanBind[LAFuture[JsCmd]] = new CanBind[LAFuture[JsCmd]] {
    def apply(future: => LAFuture[JsCmd])(ns: NodeSeq): Seq[NodeSeq] =
      ns ++ Script(OnLoad( SHtml.ajaxInvoke( () => FutureIsHere( future ) ).exp.cmd ))
  }

  def render = {
    "#future1"          #> f1 &
    "#future2"          #> f2 &
    "#render-thread *"  #> Thread.currentThread().getName
  }
}
