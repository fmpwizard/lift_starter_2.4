package com.fmpwizard.snippet

import net.liftweb.http.{S, NamedCometActorSnippet}

/**
 * This is the snippet you add on your template (see the item.html file)
 * It will setup a comet actor for the class Itemcomet and set the name to the
 * value of the query parameter item, from an url like:
 * http://127.0.0.1:8080/item?item=chromebook
 */
object AddItemComet extends NamedCometActorSnippet {
  def cometClass = "ItemComet"
  def name = S.param("item").openOr("-1")

}
