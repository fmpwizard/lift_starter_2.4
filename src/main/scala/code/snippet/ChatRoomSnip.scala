package code.snippet

import net.liftweb.http._

class ChatRoomSnip extends NamedCometActorSnippet {
  def name = ChatIn.roomMenu.currentValue.openOr("public")
  def cometClass = "ChatRoom"
}
