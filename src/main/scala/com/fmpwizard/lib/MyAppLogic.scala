package com.fmpwizard.lib

import net.liftweb.common.Loggable
import net.liftweb.actor.LAFuture

import xml.NodeSeq

object MyAppLogic extends Loggable {

  /**
   * On page render, call services to fulfill the LAFuture
   */
  def querySlowService1(la: LAFuture[NodeSeq]) {
    logger.info("querySlowService1 was called")
    Thread.sleep(9000L)
    val ns = <span class="alert alert-success">{Thread.currentThread().getName}</span>
    la.satisfy(ns)
  }


  /**
   * On page render, call services to fulfill the LAFuture
   */
  def querySlowService2(la: LAFuture[NodeSeq]) {
    logger.info("querySlowService2 was called")
    Thread.sleep(3000L)
    val ns = <span class="alert alert-success">{Thread.currentThread().getName}</span>
    la.satisfy(ns)
  }

}
