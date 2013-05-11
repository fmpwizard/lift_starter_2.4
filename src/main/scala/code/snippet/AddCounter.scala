package code.snippet

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._

object AddCounter extends Loggable {

  def render = SHtml.ajaxButton("Add Counter", () => addDynamicComet(), ("class" -> "btn btn-success")  )

  def addDynamicComet(): JsCmd = {
    val html = S.runTemplate("counter":: Nil)
    val cometId = html.map{ h =>
      h \ "div" \ "@id"
    }

    val cleanedHtmlAsString = (html.openOr( NodeSeq.Empty )).toString().encJs
    val addChatbox = """$("#counter").append(%s)""".format( cleanedHtmlAsString )

    JE.JsRaw("""lift_toWatch['%s'] = '%s'""".format( cometId.getOrElse("default-id"), nextNum ) ).cmd &
      JE.JsRaw("""console.log(lift_toWatch)""").cmd &
      JE.JsRaw(addChatbox).cmd
  }

}
