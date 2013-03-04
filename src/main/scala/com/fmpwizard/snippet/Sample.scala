package com.fmpwizard
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.actor.LAFuture

class Sample{
  import lib.FutureHelper._

  val f1: LAFuture[String] = new LAFuture()
  val f2: LAFuture[String] = new LAFuture()

  def render = {
    "#future1"          #> laFuture2Lazy( giveMeFuture1(f1, "future1") ) &
    "#future2"          #> laFuture2Lazy( giveMeFuture2(f2, "future2") ) &
    "#render-thread *"  #> Thread.currentThread().getName
  }
}
