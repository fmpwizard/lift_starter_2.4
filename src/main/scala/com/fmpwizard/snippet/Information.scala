package com.fmpwizard.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.DispatchSnippet

import com.pi4j.system.SystemInfo

object Information extends DispatchSnippet {

  def dispatch = {
    case _ => render
  }

  def render = {
    "#os *" #> SystemInfo.getOsName &
    "#os-arch *" #> SystemInfo.getOsArch &
    "#os-version *" #> SystemInfo.getOsVersion &
    "#cpu *"  #> tryo(SystemInfo.getProcessor).openOr("Not available")
  }

}
