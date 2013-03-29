package code.snippet

import net.liftweb.http._

class ChatRoomSnip extends NamedCometActorSnippet {
  def name = S.param("room").openOr("public")
  def cometClass = "ChatRoom"
}
