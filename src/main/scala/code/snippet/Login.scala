package code
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, SessionVar}
import net.liftweb.http.js.{JsCmds, JE, JsCmd}

object Login {

  def render = {
    "* [placeholder]" #> CurrentUser.is &
      "*" #> SHtml.onSubmit(s => {
        CurrentUser(s)
        DisableInput
      })
  }

}

object CurrentUser extends SessionVar[String]("Username")
case object DisableInput extends JsCmd {
  override def toJsCmd = JE.JsRaw("""$("#login").attr('disabled','')""").cmd.toJsCmd
}
