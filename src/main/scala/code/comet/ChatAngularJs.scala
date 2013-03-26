package code.comet

import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.util.ClearClearable
import xml.NodeSeq
import js.{JE, JsCmd}
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

class ChatAngularJs extends CometActor with CometListener with Loggable {

  private var msgs: InboxMessages = InboxMessages(Vector()) // private state

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

    case InboxMessages(v) =>
      sendListOrLastMessage(v)
      msgs = InboxMessages( v )

    case InitialRender =>
      partialUpdate(InitialMessages( msgs.v ))

  }

  /**
   * Clear any elements that have the clearable class.
   */
  def render = {
    ClearClearable
  }

  override def fixedRender: Box[NodeSeq] = {
    this ! InitialRender
    NodeSeq.Empty
  }

  private[this] def sendListOrLastMessage(v: Vector[String]) = {
    if ( ( v.length - msgs.v.length ) > 1 ) {
      this ! InitialRender
    } else {
      partialUpdate(NewMessageNg(v.last))
    }
  }

}

case class NewMessageNg(message: String) extends JsCmd {
  implicit val formats = DefaultFormats.lossless
  val json: JValue = ("message" -> message)
  override val toJsCmd = JE.JsRaw(""" $(document).trigger('new-ng-chat', %s)""".format( compact( render( json ) ) ) ).toJsCmd
}

