package com.fmpwizard.comet

import net.liftweb.http._
import net.liftweb.util.PassThru
import net.liftweb.http.js.JE
import net.liftweb.common.Box
import xml.NodeSeq
import java.util.Locale

class SampleComet extends CometActor {

  def render = {
    PassThru
  }

  // make this method visible so that we can initialize the actor
  override def initCometActor(theSession: LiftSession,
                              theType: Box[String],
                              name: Box[String],
                              defaultXml: NodeSeq,
                              attributes: Map[String, String]) {
    super.initCometActor(theSession, theType, name, defaultXml,
      attributes)
  }

  override def lowPriority = {
    case Data(value) =>
      partialUpdate(JE.JsRaw("""$("#messageBox").html("%s")""" format S.?(value)).cmd)
  }
}

/**
 * The case class we pass around to the comet actor
 */
case class Data(value: String)

/**
 * The session variable that keeps the current locale (as selected on the drop down on the index page
 */
object cometLocale extends SessionVar[Locale](Locale.getDefault)
