package code.comet

import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.util.ClearClearable
import xml.NodeSeq
import js.{JE, JsCmd}
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import org.joda.time.DateTime
import net.liftweb.json.ext.JodaTimeSerializers
import code.snippet.CurrentUser


class ChatRoom extends NamedCometActorTrait with Loggable {

  logger.info("here we are")
  type InboxMessages =  Vector[ChatMessage]

  private var msgs: Option[InboxMessages] = None

  override def lowPriority = {
    case message: ChatMessage =>

      msgs = msgs.map { m =>
        m ++ Vector(message )
      }
      partialUpdate(SingleChatMessage(message))

    case x => logger.info("got x " + x)

  }

  /**
   * Clear any elements that have the clearable class.
   */
  def render = {
    "#message-bind [data-ng-bind]"        #> "todo.data.message" &
    "#timestamp [data-ng-bind]"           #> "todo.data.createdAt" &
    "#messages-repeater [data-ng-repeat]" #> "todo in todos" &
    "#username  [data-ng-bind]"           #> "todo.data.username" &
    ClearClearable
  }

  override def fixedRender: Box[NodeSeq] = {
    this ! InitialRender
    NodeSeq.Empty
  }



}


case object InitialRender

case class ChatMessage(id: String, username: String, message: String, createdAt: DateTime) {
  def toJavaScript: String = {
    """{id}"""
  }
}
case class SingleChatMessage(data: ChatMessage) extends ChatRoomEvent("new-chat-message")
case class StoredChatMessage(data: List[ChatMessage]) extends ChatRoomEvent("initial-chat-messages")

class ChatRoomEvent(event: String) extends JsCmd {
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
  val json = Extraction.decompose(this)
  override def toJsCmd = JE.JsRaw("""$(document).trigger('%s', %s)""".format(event,  compact( render( json ) ) ) ).cmd.toJsCmd
}

trait JavaScriptObject[T] {
  def toJavaScript(value: T): String
}


