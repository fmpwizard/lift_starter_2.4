package code.snippet

import net.liftweb.http._

class SideChatSnip extends NamedCometActorSnippet {
  def name = ChatIn.sideRoom.openOr("side-public")
  def cometClass = "SideChat"
}

