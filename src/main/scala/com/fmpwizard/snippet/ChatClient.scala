package com.fmpwizard
package snippet

import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.js.{JsCmds, JsCmd, JE}
import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue
import actors._
import org.joda.time.DateTime

class ChatClient {

  def render = {

    val json =
      """
        {
        'name'    : %s,
        'message' : %s
        }""".format("""$('#nickname').val()""", """$('#message').val()""")

    "li *" #> List("11:57PM - ", "b") &
    "button [onclick]" #> SHtml.jsonCall(JE.JsRaw(json), ChatClient.sendChat _)
  }

}

/**
 * The logic is moved to an object, for easier testing.
 */
object ChatClient {
  def sendChat(j: JValue): JsCmd ={

    val name    = compact(render(j \ "name"))
    val message = compact(render(j \ "message"))

    InboxActor ! Message(name, message, new DateTime)

    JsCmds.Noop

  }
}
