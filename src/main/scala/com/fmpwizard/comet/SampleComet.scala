package com.fmpwizard.comet

import net.liftweb.http._
import net.liftweb.util.{Schedule, PassThru}
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JE
import com.fmpwizard.snippet.{UserCount, ShowCount}

class SampleComet extends CometActor {

  private var cnt = 0
  def render = {
    PassThru
  }


  override def lowPriority = {
    case UserCnt(cnt) =>
      partialUpdate(JE.JsRaw("""$("#userCnt").text(%s)""".format(cnt)).cmd)
      Schedule.schedule(this, Start, 2 seconds)
    case Start => (UserCount !< ShowCount).foreach{ res =>
      val currentCnt = res.asInstanceOf[Int]
      if(cnt != currentCnt){
        this ! UserCnt(currentCnt)
        cnt = currentCnt
      } else {
        Schedule.schedule(this, Start, 2 seconds)
      }

    }

  }

  override def localSetup() = {
    this ! Start
    super.localSetup()
  }

}

case class UserCnt(cnt: Int)
case object Start
