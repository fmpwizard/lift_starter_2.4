package com.fmpwizard.snippet

import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import net.liftweb.util.Helpers._
import com.fmpwizard.lib.{DispatchStreamReader, StreamReader}

class Sample{
  def render = {
    "#button1 [onclick]" #> SHtml.ajaxInvoke{ () =>
      {
        StreamReader.go
        JsCmds.Noop
      }
    }._2.cmd &
    "#button2 [onclick]" #> SHtml.ajaxInvoke{ () =>
      {
        DispatchStreamReader.go
        JsCmds.Noop
      }
      }._2.cmd
  }
}
