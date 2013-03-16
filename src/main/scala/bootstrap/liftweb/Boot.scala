package bootstrap.liftweb

import net.liftmodules.FoBo

import net.liftweb.http._
import net.liftweb.sitemap._
import net.liftweb.common._
import net.liftweb.util.NamedPF
import com.fmpwizard.utils._
import com.fmpwizard.model.LDAPUser


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    // where to search snippet
    LiftRules.addToPackages("com.fmpwizard")

    try {
      LDAPUser.ldapVendor.configure()
    }
    catch {
      case e: Exception => e.printStackTrace()
    }


    LiftRules.dispatch.prepend(NamedPF("Login Validation") {
      case Req("group_required" :: page, extension, _) if !LoginUtil.hasAuthority_?("sample_group") =>
        LoginUtil.redirectIfLogged("/login/group_not_allowed")
      case Req("login_required" :: page , extension, _) if (!LoginUtil.isLogged) =>
        () => Full(RedirectResponse("/user_mgt/login"))
    })

    // Build SiteMap
    val entries = Menu(Loc("Home", List("index"), "Home")) ::
      Menu(Loc("Restricted Login", List("login_required"), "Login required")) ::
      Menu(Loc("Restricted Group", List("group_required"), "Group required")) ::
      Menu(Loc("Group not allowed", List("login", "group_not_allowed"), "Group not allowd", List(Loc.Hidden))) ::
      LDAPUser.sitemap

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

    //We skip the FoBo built in JQuery in favor for the FoBo included lift-jquery-module
    FoBo.InitParam.JQuery=FoBo.JQuery182
    FoBo.InitParam.ToolKit=FoBo.Bootstrap222
    FoBo.init()





  } //boot

} //Boot



