package code
package snippet

import net.liftweb.util.Helpers.randomString
import net.liftweb.http.{S, NamedCometListener, SHtml}
import net.liftweb.http.js.JsCmds.SetValById
import comet.ChatMessage
import org.joda.time.DateTime
import net.liftweb.common.Loggable
import net.liftweb.http.js.JsCmd


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
class ChatIn extends Loggable {

  /**
   * The render method in this case returns a function
   * that transforms NodeSeq => NodeSeq.  In this case,
   * the function transforms a form input element by attaching
   * behavior to the input.  The behavior is to send a message
   * to the ChatServer and then returns JavaScript which
   * clears the input.
   */
  val room = S.param("room").openOr("public")
  def render = SHtml.onSubmit( sendMessage _ )


  def sendMessage(s: String): JsCmd =  {
    val message = ChatMessage( randomString(8), CurrentUser.is, s, new DateTime()  )
    logger.info("room: " + room)

    NamedCometListener.getDispatchersFor( Some(room) ).foreach(_.foreach(actor => actor ! message ))
    SetValById("chat_in", "")
  }
}
