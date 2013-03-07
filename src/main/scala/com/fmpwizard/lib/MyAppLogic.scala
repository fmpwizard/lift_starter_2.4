package com.fmpwizard.lib

import net.liftweb.common.Loggable
import net.liftweb.actor.LAFuture
import net.liftweb.http.js.{JE, JsCmd}
import net.liftweb.http.js.JsCmds.SetHtml
import xml.Text

object MyAppLogic extends Loggable {

  /**
   * On page render, call services to fulfill the LAFuture
   */
  def querySlowService1(la: LAFuture[JsCmd], id: String) {
    logger.info("querySlowService1 was called")
    Thread.sleep(9000L)
    val js = SetHtml(id, Text(Thread.currentThread().getName)) & AddCss(id)
    la.satisfy(js)
  }


  /**
   * On page render, call services to fulfill the LAFuture
   */
  def querySlowService2(la: LAFuture[JsCmd], id: String) {
    logger.info("querySlowService2 was called")
    Thread.sleep(3000L)
    val js = SetHtml(id, Text(Thread.currentThread().getName)) & AddCss(id)
    la.satisfy(js)
  }

}

case class AddCss(id: String) extends JsCmd {
  override val toJsCmd = JE.JsRaw("""$("#%s").attr("class", "alert alert-success")""" format id).cmd.toJsCmd
}
