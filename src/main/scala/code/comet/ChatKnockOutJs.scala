package code
package comet

import net.liftweb.http.{CometListener, CometActor}
import net.liftweb.util.ClearClearable
import net.liftweb.http.js.{JE, JsCmd}
import net.liftweb.json._
import net.liftweb.json.ext.JodaTimeSerializers


/**
 * The screen real estate on the browser will be represented
 * by this component.  When the component changes on the server
 * the changes are automatically reflected in the browser.
 */
class ChatKnockOutJs extends CometActor with CometListener {
  private var msgs: Vector[ChatMessage] = Vector() // private state

  /**
   * When the component is instantiated, register as
   * a listener with the ChatServer
   */
  def registerWith = ChatServer

  /**
   * The CometActor is an Actor, so it processes messages.
   * In this case, we're listening for Vector[String],
   * and when we get one, update our private state
   * and reRender() the component.  reRender() will
   * cause changes to be sent to the browser.
   */
  override def lowPriority = {
    case v: Vector[ChatMessage] =>
      msgs = v
      partialUpdate(NewMessageKo(v.last))
  }

  /**
   * Clear any elements that have the clearable class.
   */
  def render = ClearClearable
}

case class NewMessageKo(message: ChatMessage) extends JsCmd {

  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
  val json = Extraction.decompose(message)
  override val toJsCmd = JE.JsRaw(""" $(document).trigger('new-ko-chat', %s)""".format( compact( render( json ) ) ) ).toJsCmd
}
