package com.fmpwizard
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.actor._
import net.liftweb.common.Loggable
import xml.NodeSeq
import lib.LiftHelper._
import lib.MyAppLogic._

class Sample extends Loggable {

  val f1: LAFuture[NodeSeq] = new LAFuture()
  val f2: LAFuture[NodeSeq] = new LAFuture()

  LAScheduler.execute( () => querySlowService1( f1 ) )
  LAScheduler.execute( () => querySlowService2( f2 ) )

  def render = {
    "#future1"          #> f1 &
    "#future2"          #> f2 &
    "#render-thread *"  #> Thread.currentThread().getName
  }
}
