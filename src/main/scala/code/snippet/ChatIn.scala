package code
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml

import comet.{ChatMessage, ChatServer}
import net.liftweb.http.js.JsCmds.SetValById
import org.joda.time.DateTime
import net.liftweb.http.js.{JsExp, JsCmd, JE}
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.DefaultFormats

/**
 * A snippet transforms input to output... it transforms
 * templates to dynamic content.  Lift's templates can invoke
 * snippets and the snippets are resolved in many different
 * ways including "by convention".  The snippet package
 * has named snippets and those snippets can be classes
 * that are instantiated when invoked or they can be
 * objects, singletons.  Singletons are useful if there's
 * no explicit state managed in the snippet.
 */
object ChatIn {

  /**
   * The render method in this case returns a function
   * that transforms NodeSeq => NodeSeq.  In this case,
   * the function transforms a form input element by attaching
   * behavior to the input.  The behavior is to send a message
   * to the ChatServer and then returns JavaScript which
   * clears the input.
   */
  def render = {
    "* [onclick]" #> SHtml.jsonCall(GetDataFromPage, submitMessage _)
  }

  private def submitMessage(j: JValue): JsCmd = {
    implicit val format = DefaultFormats
    val msg = j.extractOpt[String]
    msg.foreach(m =>  ChatServer ! ChatMessage(randomString(6), CurrentUser.is, m, new DateTime()) )
    SetValById("chat_in", "")
  }


}


case object GetDataFromPage extends JsExp {
  override def toJsCmd = JE.JsRaw("""$("#chat_in").val()""").toJsCmd
}
