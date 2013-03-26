package code
package comet

import net.liftweb._
import http._
import js.{JE, JsCmd}
import util._
import Helpers._


/**
 * The screen real estate on the browser will be represented
 * by this component.  When the component changes on the server
 * the changes are automatically reflected in the browser.
 */
class Chat extends CometActor with CometListener {
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
    case data@ InboxMessages(v) =>
      msgs = data
      partialUpdate(NewMessage(v.last))
  }

  /**
   * Clear any elements that have the clearable class.
   */
  def render = ClearClearable
}

case class NewMessage(message: String) extends JsCmd {
  override val toJsCmd = JE.JsRaw(""" $('#messages').append('<li>%s</li>')""".format( unquote( encJs( message ) ) )).toJsCmd
}
