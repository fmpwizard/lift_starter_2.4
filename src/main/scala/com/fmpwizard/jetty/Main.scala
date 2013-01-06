package com.fmpwizard.jetty

import java.io.{FileInputStream, File}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import net.liftweb.util.Props
import net.liftweb.util.Helpers._

/**
 * This is to run the embedded jetty
 */
object Main {

  def main(args: Array[String]): Unit = {

    //Props.whereToLook = () => ("production.raspberry.props", () => tryo(new FileInputStream(System.getProperty("Props.path")))) :: Nil
    val port = Props.getInt("jetty.port", 80)
    val server = new Server(port)
    val webctx = new WebAppContext
    val webtmpdir = "/tmp"
    val webappDirInsideJar = webctx.getClass.getClassLoader.getResource("webapp").toExternalForm
    webctx.setWar(webappDirInsideJar)
    webctx.setContextPath("/")
    webctx.setExtractWAR(true)
    webctx.setTempDirectory(new File(webtmpdir))
    server.setHandler(webctx)
    server.start
    server.join
  }
}
