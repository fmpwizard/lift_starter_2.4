package code.comet

import net.liftweb.http.CometActor
import net.liftweb.common.Loggable
import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.http.js.{JE, JsCmd}

class Counter extends CometActor with Loggable {

  var _counter = 0

  def render = {
    "#name *"       #> name &
    "#name [id]"    #> name.map( "name-" + _) &
    "#number [id]"  #> name.map( "number-" + _)
  }

  override def lowPriority = {
    case Ping =>
      _counter = _counter + 1
      partialUpdate( UpdateCounter( _counter, name.openOr("global") ) )
      Schedule.schedule(this, Ping, 3 seconds)
  }

  override def localSetup() {
    Schedule.schedule(this, Ping, 2 seconds)
    super.localSetup()
  }

}

case object Ping

case class UpdateCounter(n: Int, name: String) extends JsCmd {
  val cnt = Helpers.encJs(n.toString)
  override def toJsCmd = JE.JsRaw("""$("#number-%s").text(%s)""".format( name, cnt  )).cmd.toJsCmd
}
