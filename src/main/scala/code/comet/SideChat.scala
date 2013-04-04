package code.comet

import net.liftweb.http.CometActor
import net.liftweb.util.{Schedule, PassThru}
import net.liftweb.util.Helpers._
import net.liftweb.http.js.{JE, JsCmds}
import net.liftweb.builtin.snippet.AsyncRenderComet

class SideChat extends AsyncRenderComet {


  override def lowPriority: PartialFunction[Any, Unit] = {

    case Ping =>
      println("here 3")
      Schedule.schedule( this, Ping , 10 seconds  )
      println("got it!")
      partialUpdate( JE.JsRaw( """console.log("here.")""" ).cmd )

  }

  override def render = {
    println("render called")
    PassThru
  }

  override def localSetup()  {
    Schedule.schedule( this, Ping , 3 seconds  )
    println("local setup called")
    super.localSetup()
  }
}

case object Ping
