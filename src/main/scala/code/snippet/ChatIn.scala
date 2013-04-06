package code
package snippet

import comet._

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds.SetValById
import net.liftweb.util.Helpers.randomString
import net.liftweb.sitemap.Menu

import org.joda.time.DateTime
import net.liftweb.util.Helpers


object ChatIn extends Loggable {


  lazy val roomMenu = Menu.param[String]("Room", "Room", s => Full(s), s => s) / "rooms"
  def room = roomMenu.currentValue
  def sideRoom = roomMenu.currentValue.map("side-" + _ )

  def render = SHtml.onSubmit( sendMessage _ )

  def sideChat = SHtml.onSubmit( sendSideMessage _ )


  def sendMessage(s: String): JsCmd =  {
    val message = ChatMessage( randomString(8), CurrentUser.is, s, new DateTime(), room.openOr("public")  )
    Storage ! AddMessage( message )
    NamedCometListener.getDispatchersFor( room ).foreach(_.foreach(actor => actor ! message ))
    SetValById("chat_in", "")
  }

  def sendSideMessage(s: String): JsCmd =  {
    val message = ChatMessage( randomString(8), CurrentUser.is, s, new DateTime(), sideRoom.openOr("side-public")  )
    Storage ! AddMessage( message )
    NamedCometListener.getDispatchersFor( sideRoom ).foreach(_.foreach(actor => actor ! message ))
    SetValById("side-chat-in", "")
  }


}
