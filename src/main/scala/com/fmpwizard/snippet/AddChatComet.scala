package com.fmpwizard.snippet

import net.liftweb.http.NamedCometActorSnippet


class AddChatComet extends NamedCometActorSnippet{
  def name = "chat"
  def cometClass = "ChatClient"
}
