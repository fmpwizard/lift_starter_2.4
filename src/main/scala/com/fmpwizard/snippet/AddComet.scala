package com.fmpwizard
package snippet

import net.liftweb.http.NamedCometActorSnippet

class AddComet extends NamedCometActorSnippet{
  def cometClass = "DoorControl"
  def name = "front-door"
}
