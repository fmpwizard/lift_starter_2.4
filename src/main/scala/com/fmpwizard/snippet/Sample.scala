package com.fmpwizard
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.actor.LAFuture
import net.liftweb.common.Loggable

class Sample extends Loggable {
  import lib.FutureHelper._
  import lib.MyAppLogic._

  val f1: LAFuture[String] = new LAFuture()
  val f2: LAFuture[String] = new LAFuture()

  def render = {
    "#future1"          #> laFuture2Lazy(f1,  querySlowService1, giveMeFuture1, "future1" ) &
    "#future2"          #> laFuture2Lazy(f2,  querySlowService2, giveMeFuture2, "future2" ) &
    "#render-thread *"  #> Thread.currentThread().getName
  }
}
