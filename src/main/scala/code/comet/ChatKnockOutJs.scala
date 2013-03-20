package code
package comet

import net.liftweb._
import common._
import http._
import http.js.{JsCmds, JE, JsCmd}
import json._
import json.JsonDSL._
import util._
import Helpers._
import xml.NodeSeq
import comet.NewMessageKo
import comet.InitialMessages


/**
 * The screen real estate on the browser will be represented
 * by this component.  When the component changes on the server
 * the changes are automatically reflected in the browser.
 */
class ChatKnockOutJs extends CometActor with CometListener with Loggable {


  private var msgs: Vector[String] = Vector() // private state

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
    case v: Vector[String] =>
      msgs = v
      partialUpdate(NewMessageKo(v.last))

    case InitialRender =>
      partialUpdate(InitialMessages( msgs ))
  }

  /**
   * Clear any elements that have the clearable class.
   */
  def render = {
    ClearClearable
  }

  override def fixedRender: Box[NodeSeq] = {
    S.session map { sess =>
      sess.addPostPageJavaScript( () => JsCmds.Alert("hi") )
    }
    this ! InitialRender
    NodeSeq.Empty
  }

}

case class NewMessageKo(message: String) extends JsCmd {
  implicit val formats = DefaultFormats.lossless
  val json: JValue = ("message" -> message)
  override val toJsCmd = JE.JsRaw(""" $(document).trigger('new-ko-chat', %s)""".format( compact( render( json ) ) ) ).toJsCmd
}

case class InitialMessages(messages: Vector[String]) extends JsCmd {
  implicit val formats = DefaultFormats.lossless
  val json: JValue = messages.map{ m =>
    ("message" -> m)
  }
  override val toJsCmd = JE.JsRaw(""" $(document).trigger('initial-chat-messages', %s)""".format( compact( render( json ) ) ) ).toJsCmd
}

case object InitialRender
