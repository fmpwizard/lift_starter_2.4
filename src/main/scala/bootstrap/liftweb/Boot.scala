package bootstrap.liftweb

import net.liftweb._
import sitemap.Loc._
import util._

import common._
import http._
import sitemap._

import net.liftmodules.FoBo
import xml.NodeSeq


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    //Conexion(host = "ciriscr.com", baseDatos = "Africa")
    //RegisterJodaTimeConversionHelpers()

    // where to search snippet
    LiftRules.addToPackages("com.fmpwizard")


    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(Paths.sitemap)


    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // set DocType to HTML5
    LiftRules.htmlProperties.default.set((r: Req) =>new Html5Properties(r.userAgent))

    FoBo.InitParam.ToolKit=FoBo.Bootstrap204
    FoBo.init()

  } //boot

} //Boot

object Paths {

  val index = Menu.i("Index") / "index"
  val page2 = Menu.i("page2") / "index2"
  val page3 = Menu.i("page3") / "index3"
  val page4 = Menu.i("page4") / "index4"
  val page5 = Menu.i("page5") / "index5"

  def sitemap = SiteMap(
    index >> Snippet("mysnippet", addDivider _ )  submenus(
      page2,
      page3
    ),
    page4,
    page5
  )

  def addDivider( ns: NodeSeq): NodeSeq ={
    println("we got " + ns)
    ns
  }

}
