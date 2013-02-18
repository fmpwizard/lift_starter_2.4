package com.fmpwizard.snippet

import net.liftweb.http._
import net.liftweb.http.js.{JE, JsCmds, JsCmd}
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.{Script, OnLoad}
import net.liftweb.actor.LAFuture
import net.liftweb.http.rest.RestContinuation
import net.liftweb.common.Full
import net.liftweb.common.Full

class Sample{

  val f1: LAFuture[String] = new LAFuture()

  /**
   * Imagine this is a call to a 3rd party service that takes a long time.
   */
  def showFuture: JsCmd = {

    val req = S.request
    if ( req.map(_.request.suspendResumeSupport_?) == Full( true ) ) {
      //S.request.map(_.request.suspend(LiftRules.cometGetTimeout))
      S.request.map(_.request.suspend(3000L))
      //This is our super computation, goes off and takes a huge amount of time, a full 3 seconds.
      //Thread.sleep(3000L)
      f1.satisfy(Thread.currentThread().getName)
      val js = JE.JsRaw("""$("#result-from-external-service").text("This task run on the thread name: %s")""".format(f1.get(4000L))).cmd &
        JE.JsRaw("""$("#result-from-external-service").attr("class", "alert alert-success")""").cmd

      //S.request.map(_.request.resume(req.openOr(Req.nil), JavaScriptResponse( js ) ))
      JsCmds.Noop
      js
    } else {
      //We just give up if the container does not support continuation.
      JsCmds.Noop
    }
  }

  /*def showFuture: JsCmd = {
    Thread.sleep(3000L)
    f1.satisfy(Thread.currentThread().getName)
    JE.JsRaw("""$("#result-from-external-service").text("This task run on the thread name: %s")""".format(f1.get(4000L))).cmd &
    JE.JsRaw("""$("#result-from-external-service").attr("class", "alert alert-success")""").cmd
  }*/

  def render = {
    "#future"           #> Script(OnLoad(SHtml.ajaxInvoke(() => showFuture).exp.cmd) ) &
    "#render-thread *"  #> Thread.currentThread().getName
  }


}
