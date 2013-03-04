package com.fmpwizard
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.actor.LAFuture
import net.liftweb.common.Loggable
import xml.NodeSeq

class Sample extends Loggable {

  val f1: LAFuture[String] = new LAFuture()
  val f2: LAFuture[String] = new LAFuture()

  def render = {
    "#future1 *"        #> "Loading Future 1" &
    "#future2 *"        #> "Loading future 2" &
    "#render-thread *"  #> Thread.currentThread().getName &
    "#js"               #> AddFutureCallback

  }

  object AddFutureCallback extends Function1[NodeSeq, NodeSeq] {
    import lib.FutureHelper._
    import lib.MyAppLogic._

    def apply(in: NodeSeq): NodeSeq = {
      laFuture2Lazy(f1,  querySlowService1, giveMeFuture1, "future1" ) ++
      laFuture2Lazy(f2,  querySlowService2, giveMeFuture2, "future2" )
    }
  }
}
