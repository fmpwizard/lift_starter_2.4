package code
package comet

import net.liftweb._
import common.Loggable
import http._
import js.{JsCmds, JE, JsCmd}
import json._
import json.JsonDSL._
import util._
import Helpers._
import xml.NodeSeq


/**
 * The screen real estate on the browser will be represented
 * by this component.  When the component changes on the server
 * the changes are automatically reflected in the browser.
 */
class ChatKnockOutJs extends CometActor with Loggable {

  override def lowPriority = {
    case s => s
  }
  /**
   * Clear any elements that have the clearable class.
   */
  def render = {
    ClearClearable
  }

  override def fixedRender = {
    S.session map { sess =>
      sess.addPostPageJavaScript( () => JsCmds.Alert("hi") )
    }
    NodeSeq.Empty
  }
}

