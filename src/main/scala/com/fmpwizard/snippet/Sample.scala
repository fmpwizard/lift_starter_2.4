package com.fmpwizard.snippet

import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import net.liftweb.util.Helpers._
import com.fmpwizard.lib.StreamReader

class Sample{
  def render = {
    "#button [onclick]" #> SHtml.ajaxInvoke{ () =>
      {
        StreamReader.go
        JsCmds.Noop
      }
    }._2.cmd
  }
}
