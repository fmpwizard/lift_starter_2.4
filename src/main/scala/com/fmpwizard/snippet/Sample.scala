package com.fmpwizard.snippet

import net.liftweb.http.SHtml
import net.liftweb.http.js.{JsCmd, JE, JsCmds}
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.{Function, Script}
import net.liftweb.http.js.JE.JsVar
import net.liftweb.common.{Loggable, Full}

object Sample extends Loggable{

  /**
   * JavaScript to collect our form data
   */
  val js1 =
    """
      |window.dyTable = new window.fmpwizard.views.DynamicFields();
      |window.dyTable.collectFormData();
    """.stripMargin
  /**
   * JavaScript to setup the adding rows to the page action
   */
  val js2 =
    """
      |            $(document).ready(function() {
      |              $('#btnDel').attr('disabled','disabled');
      |              window.dyTable = new window.fmpwizard.views.DynamicFields();
      |              window.dyTable.addFields();
      |              window.dyTable.removeFields();
      |            });
    """.stripMargin

  
  
  def render = {
    "#next [onclick]" #> JE.JsRaw(js1)
  }

  def sendToServer = {
    "#sendToServer" #> Script(
      Function("sendDataToServer", List("paramName"),
        SHtml.jsonCall(JsVar("paramName"), s => addRowsToDB(s) )._2.cmd
      )
    ) &
    "#initDynamic" #> Script(JE.JsRaw(js2).cmd)
  }


  /**
   * Logic to process the data submitted
   */
  private def addRowsToDB(x: Any) : JsCmd ={
    val list = Full(x).asA[List[List[String]]]
    /**
     * The list we get has only one row, this one row has
     * a list of two rows, the textfield, and the numeric field.
     */
    val res = list.map{singleRowList =>
      singleRowList.map{ entry =>
        for {
          text      <- entry.headOption
          schedule  <- entry.tail.headOption
          days      <- asInt(schedule)
        } yield {
          logger.info("The text we got was: %s and the related field value was: %s".format(text, days))
          //This is where you can store the data on a database
          (text + " => " + days.toString)
        }
      }
    }
    JsCmds.Alert("The server got: %s" format res)
  }


}
