package com.fmpwizard.snippet

import net.liftweb.http.SHtml
import net.liftweb.http.js.{JsCmd, JE, JsCmds}
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.{Function, Script}
import net.liftweb.http.js.JE.JsVar
import net.liftweb.common.{Loggable, Full}
import net.liftweb.json.JsonAST._
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.JString
import net.liftweb.common.Full
import net.liftweb.json.JsonAST.JInt
import net.liftweb.http.js.JE.JsVar
import net.liftweb.json.JsonAST.JArray

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
        SHtml.jsonCall(JsVar("paramName"), (s: JValue) => addRowsToDB(s) )._2.cmd //use on lift >= 2.5
        //SHtml.jsonCall(JsVar("paramName"), (s: Any) => addRowsToDB(s) )._2.cmd //Use this on Lift < 2.5
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
          //This is where you can store the data on a database.
          (text + " => " + days.toString)
        }
      }
    }
    JsCmds.Alert("The server got: %s" format res)
  }

  /**
   * Logic to process the data submitted
   */
  private def addRowsToDB(x: JValue) : JsCmd ={
    //JArray(List(JArray(List(JString(w), JString(1))), JArray(List(JString(w), JString(1)))))
    logger.info(x)
    val res = for {
      JArray(child) <- x
      JArray(List(JString(text), JString(n))) <- child
    } yield{
      logger.info("The text we got was: %s and the related field value was: %s".format(text, n.toInt))
      //This is where you can store the data on a database.
      (text, n.toInt)
    }
    logger.info(res)
    JsCmds.Alert("The server got %s" format res)
  }
}
