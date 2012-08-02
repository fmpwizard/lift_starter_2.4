package com.fmpwizard.snippet

import net.liftweb.http.{S, SHtml}
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.{SetHtml, Function, Script}
import net.liftweb.textile._
import net.liftweb.http.js.JE.{Call, JsVar}
import net.liftweb.http.js.{JE, JsCmd}
import net.liftweb.common.{Full, Loggable}
import com.fmpwizard.model.TextTable
import com.fmpwizard.utils.Paths
import net.liftweb.util.Helpers


class Sample extends Loggable{

  val text     = Paths.post.toLoc.currentValue.map(_.content.is)
  val blogPost = Paths.post.toLoc.currentValue.dmap(TextTable.create)(x => x)

  def render = {
    "#link" #> SHtml.button("Create new post", () => create)
  }

  /**
   * Create a new record and redirect to it
   */
  private def create ={
    val id= TextTable.create.saveMe().blogID.is
    S.redirectTo("/post/" + id)
  }

  /**
   * Add our parsing JavaScript function to the page
   * Set the onkeyup listener, so that we update the preview box ans save to the database as we type content
   * And finally load textile data from the database if we reloaded the page or click on a link.
   */
  def sendToServer = {
    "#sendToServer" #> Script(
      Function("updatePreview", List("paramName"),
        SHtml.ajaxCall(JE.JsRaw("""$("#markItUp").val()"""), (s: String) => {
          val rowID= Sample.save(blogPost, s)
          Sample.parse(s, rowID)
        } )._2.cmd
      )
    ) &
    "#markItUp [onkeyup]" #> Call("updatePreview") &
    "#markItUp *"         #> text.map( Helpers.blankForNull(_))
  }
}


/**
 * I'm getting in the habit of moving logic to an object, which makes testing easier.
 */
object Sample extends Loggable{
  /**
   * Here we parse the textile text and convert it into html
   * We also set the url of the current document, not the SetHtml("link" ... )
   */
  def parse(s: String, id: Long): JsCmd = {
    logger.info("We got from the web: %s".format(s))
    val parsed= TextileParser.toHtml(s)
    SetHtml("markItUpResult", parsed) &
    SetHtml("link", SHtml.link(Paths.post.toLoc.currentValue.dmap("1")(_.blogID.is.toString) , ()=> Unit, <span>Visit this blog id: {id}</span> ))
  }

  /**
   * This is where you can save the data to the database
   */
  def save(r: TextTable, s: String): Long ={
    logger.info("We save the data: %s".format(s))
    val entry = r.content(s).saveMe()
    logger.info("The row id is: %s".format(entry.blogID.is))
    entry.blogID.is
  }
}
