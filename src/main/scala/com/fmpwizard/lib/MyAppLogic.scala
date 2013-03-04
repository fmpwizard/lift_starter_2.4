package com.fmpwizard.lib

import net.liftweb.common.Loggable
import net.liftweb.actor.LAFuture
import net.liftweb.http.js.JsCmd

object MyAppLogic extends Loggable {

  /**
   * On page render, call services to fulfill the LAFuture
   */
  def querySlowService1(la: LAFuture[String]) {
    logger.info("querySlowService1 was called")
    Thread.sleep(9000L)
    la.satisfy(Thread.currentThread().getName)
  }

  /**
   * This gets called from browser by an ajax call.
   * It will take the result from the LAFuture and put it on the page
   */
  def giveMeFuture1(la: LAFuture[String], id: String ): JsCmd = {
    FutureIsHere( la, id )
  }


  /**
   * On page render, call services to fulfill the LAFuture
   */
  def querySlowService2(la: LAFuture[String]) {
    logger.info("querySlowService2 was called")
    Thread.sleep(3000L)
    la.satisfy(Thread.currentThread().getName)
  }

  /**
   * This gets called from browser by an ajax call.
   * It will take the result from the LAFuture and put it on the page
   */
  def giveMeFuture2(la: LAFuture[String], id: String ): JsCmd = {
    FutureIsHere( la, id )
  }

}
