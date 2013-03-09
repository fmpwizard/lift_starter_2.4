package com.fmpwizard.lib


import net.liftweb.actor._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.util.CanBind
import net.liftweb.http.js.JsCmds._
import net.liftweb.util.Helpers._


import xml.{Node, Elem, NodeSeq}

/**
 * This could be part of Lift
 * @param la the LAFuture holding the NodeSeq to update the UI
 */

case class FutureIsHere(la: LAFuture[NodeSeq], id: String) extends JsCmd with Loggable {

  logger.debug(id)

  val updatePage: JsCmd = if (la.isSatisfied) {
    Replace(id , la.get)
  } else {
    tryAgain()
  }

  private def tryAgain(): JsCmd = {
    val funcName: String = S.request.flatMap(_._params.toList.headOption.map(_._1)).openOr("")
    val retry = "setTimeout(function(){liftAjax.lift_ajaxHandler('%s=true', null, null, null)}, 3000)".format(funcName)
    JE.JsRaw(retry).cmd
  }

  override val toJsCmd = updatePage.toJsCmd
}

object LiftHelper extends Loggable {
  implicit def laFutureNSTransform: CanBind[LAFuture[NodeSeq]] = new CanBind[LAFuture[NodeSeq]] {
    def apply(future: => LAFuture[NodeSeq])(ns: NodeSeq): Seq[NodeSeq] = {
      val elem: Option[Elem] = ns match {
        case e: Elem => Some(e)
        case nodeSeq if nodeSeq.length == 1 && nodeSeq(0).isInstanceOf[Elem] => Box.asA[Elem](nodeSeq(0))
        case nodeSeq => None
      }

      val id: String = elem.map(_.attributes.filter(att => att.key == "id")).map{ meta =>
        tryo(meta.value.text).getOrElse( nextFuncName )
      } getOrElse{
        ""
      }

      val ret: Option[NodeSeq] = ns.toList match {
        case head :: tail => {
          elem.map{ e =>
            e % ("id" -> id) ++ tail ++ Script(OnLoad( SHtml.ajaxInvoke( () => FutureIsHere( future, id ) ).exp.cmd ))
          }
        }

        case empty => None
      }

      ret getOrElse NodeSeq.Empty
    }
  }
}
