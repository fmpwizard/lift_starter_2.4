package com.fmpwizard
package comet

import net.liftweb.http._
import js.{JsCmds, JsCmd, JE}
import net.liftweb.util.Helpers._
import model._
import net.liftweb.common.Full

/**
 * Comet actor that displays the item id and updated price
 */
class ItemComet extends NamedCometActorTrait {

  /**
   * We get the item id from the url query parameter, a sample url is:
   * http://127.0.0.1:8080/item?item=12
   */
  val item = S.param("item").openOr("N/A")
  /**
   * This simulates a Database call
   */
  ItemDB.items.putIfAbsent(item, 0.0)

  /**
   * Displays the initial state (price and item name)
   * It also wires up the button to update the price
   */
  def render = {
    val price = ItemDB.items.get(item)
    "#price *+"      #> price &
    "#itemName *"   #> item &
    "#update"       #> SHtml.ajaxButton("Increase", () => increasePrice())
  }

  /**
   * The two messages that our comet actor listens for.
   */
  override def lowPriority = {
    case Price(newPrice) => partialUpdate(JE.JsRaw("""$("#price").html("$%s")""".format(newPrice)).cmd)
    case Sold            => partialUpdate(JE.JsRaw("""$("#status").html("Sold.")""").cmd)
  }

  /**
   * The method that is called when the "Increase" button is presssed.
   * We could return any JavaScript, but here we simply return a Noop (nothing)
   */
  private def increasePrice(): JsCmd = {
    val price = ItemDB.items.get(item) + 1
    ItemDB.items.replace(item, price)
    NamedCometListener.getDispatchersFor(Full(item)).foreach(_.foreach(_ ! Price(price)))
    JsCmds.Noop
  }

}

