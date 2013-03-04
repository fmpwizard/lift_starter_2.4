package com.fmpwizard.lib

import net.liftweb.http.js.{JE, JsCmd}
import xml.NodeSeq
import net.liftweb.http.js.JsCmds.{CmdPair, OnLoad, Script}
import net.liftweb.http.SHtml
import net.liftweb.actor.LAFuture
import net.liftweb.common.{Loggable, Box}
import org.joda.time.DateTime

object FutureHelper extends Loggable{
  def laFuture2Lazy( func: => JsCmd ) : NodeSeq =
    Script(OnLoad( SHtml.ajaxInvoke( () => func ).exp.cmd ))

  private def threadName(la: LAFuture[String]): Box[String] = {
    Thread.sleep(3000L)
    la.satisfy(Thread.currentThread().getName)
    la.get(4000L)
  }

  private def todaysDay(la: LAFuture[String]): Box[String] = {
    Thread.sleep(3000L)
    la.satisfy((new DateTime).toString)
    la.get(4000L)
  }

  /**
   * Imagine this is a call to a 3rd party service that takes a long time.
   */
  def giveMeFuture1(la: LAFuture[String], id: String ): JsCmd = {
    logger.info("giveMeFuture1 was called")
    val ret= threadName(la)
    FutureIsHere( ret.openOr("Failed to get the future."), id )
  }

  /**
   * Imagine this is a call to a 3rd party service that takes a long time.
   */
  def giveMeFuture2(la: LAFuture[String], id: String ): JsCmd = {
    logger.info("giveMeFuture2 was called")
    val ret= todaysDay(la)
    FutureIsHere( ret.openOr("Failed to get the future."), id )
  }
}

case class FutureIsHere(str: String, idSelector: String ) extends JsCmd {
  val replace = JE.JsRaw("""$("#%1$s").replaceWith('<span id="%1$s">Data: %2$s"</span>')"""
    .format(idSelector, str)
  ).cmd

  val updateCssClass = JE.JsRaw("""$("#%s").attr("class", "alert alert-success")""" format idSelector).cmd

  override val toJsCmd = CmdPair(replace, updateCssClass).toJsCmd
}
