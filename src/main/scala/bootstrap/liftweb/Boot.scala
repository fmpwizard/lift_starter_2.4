package bootstrap.liftweb

import net.liftweb._
import util._

import common._
import http._
import js.JE
import provider.HTTPRequest
import sitemap._
import com.fmpwizard.comet.{cometLocale, Data, SampleComet}
import xml.NodeSeq
import java.util.Locale


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    // where to search snippet
    LiftRules.addToPackages("com.fmpwizard")

    // Build SiteMap
    val entries = List(
      Menu.i("Index") / "index"
    )

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))


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

    //On my page I have both, and ajax component and a comet, so I set both reload the page on lost session
    LiftRules.noAjaxSessionCmd.default.set(JE.JsRaw("""window.location.reload(true);""").cmd)
    LiftRules.noCometSessionCmd.default.set(JE.JsRaw("""window.location.reload(true);""").cmd)

    /**
     * This isn't used much, it is better to override def localSetup()
     */
    LiftRules.cometCreation.append{
      case info@ CometCreationInfo("SampleComet",name,html,attr,session) =>
        val comet = new SampleComet
        comet.initCometActor(session, Full("SampleComet"), name, html, attr)
        comet ! Data("It's me!")
        comet
    }

    //Store the locale in a sessionVar, so we can access from a comet
    LiftRules.localeCalculator = (req: Box[HTTPRequest]) => cometLocale.is

  } //boot

} //Boot



