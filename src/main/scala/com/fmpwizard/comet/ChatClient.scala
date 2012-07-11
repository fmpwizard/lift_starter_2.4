package com.fmpwizard
package comet

import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.http.{NamedCometActorTrait, SHtml}
import net.liftweb.http.js.{JsCmds, JsCmd, JE}
import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue
import actors._
import org.joda.time.DateTime
import service._
import net.liftweb.http.js.jquery.JqJsCmds.AppendHtml
import xml.Text
import net.liftweb.common.Logger

class ChatClient extends NamedCometActorTrait{

  private[this] var renderedMessages: Vector[MessageRow] = Vector(MessageRow(new DateTime(), "me", "Starting message", "Home"))

  def render = {

    val json =
      """
        {
        'name'    : %s,
        'message' : %s
        }""".format( """$('#nickname').val()""", """$('#message').val()""")

      "button [onclick]" #> SHtml.jsonCall(JE.JsRaw(json), ChatClient.sendChat _) &
      "li" #> renderedMessages.map(x => "* *" #> "%s - %s: %s - from %s".format(x.dateTime.toString("HH:mm:ss"), x.user, x.message, x.server) )
  }


  override def lowPriority = {
    case x @ MessageRow(datetime, user, msg, server) => {
      renderedMessages = renderedMessages :+ x

      partialUpdate(AppendHtml(
        "messages",
        <li>{Text(  "%s - %s: %s - from %s".format(datetime.toString("HH:mm:ss"), user, msg, server))}</li>
      ))
    }
  }


}

/**
 * The logic is moved to an object, for easier testing.
 */
object ChatClient extends Logger{
  implicit val format = DefaultFormats

  case class m(name: String, msg: String)

  def sendChat(j: JValue): JsCmd = {
    val name = (j \ "name").extract[String]
    val message = (j \ "message").extract[String]

    info("We got %s" format j)

    InboxActor ! Message(name, message, new DateTime)

    JE.JsRaw("""$('#message').val('')""").cmd
    //JsCmds.Noop

  }
}
