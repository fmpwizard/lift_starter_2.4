package code
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, SessionVar}
import net.liftweb.http.js.{JsCmds, JE, JsCmd}
import net.liftweb.util._
import net.liftweb.json._
import net.liftweb.json.ext.JodaTimeSerializers

object Login {

  def render = {

    def disable: CssSel = {
      if (CurrentUser.is != "Username") {
        "* [disabled]" #> "disabled"
      } else {
        "nah" #> "noop"
      }
    }

    "* [placeholder]" #> CurrentUser.is &
      "*" #> SHtml.onSubmit(s => {
        CurrentUser(s)
        DisableInput & UserLoggedIn(s)
      }) &
      disable
  }

}

object CurrentUser extends SessionVar[String]("Username")

case object DisableInput extends JsCmd {
  override def toJsCmd = JE.JsRaw("""$("#login").attr('disabled','')""").cmd.toJsCmd
}

case class UserLoggedIn(userId: String) extends JsCmd {
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
  val json = Extraction.decompose(this)
  override def toJsCmd = JE.JsRaw("""$(document).trigger('%s', %s)""".format("current-user",  compact( render( json ) ) ) ).cmd.toJsCmd
}

