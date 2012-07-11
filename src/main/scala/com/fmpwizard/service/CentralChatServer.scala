package com.fmpwizard
package service

import actors._
import com.twitter.finagle.ServiceFactory
import org.jboss.netty.handler.codec.http._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Http
import com.twitter.util.TimeConversions._
import actors._
import net.liftweb.util.Helpers


object CentralChatServer {

  val hostAndPort = "fmpwizard.iriscouch.com:6984"
  /**
   * You init a clientFactory only once and use it several times across your application
   */
  val clientFactory: ServiceFactory[HttpRequest, HttpResponse] = ClientBuilder()
    .codec(Http())
    .tls("fmpwizard.iriscouch.com")
    .hosts(hostAndPort)
    .tcpConnectTimeout(1.second)
    .hostConnectionLimit(1)
    .buildFactory()

  def sendToCouchDB(m: Message) ={

    val path = "/chatserver/%s".format(Helpers.nextFuncName)
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, path)
    request.setHeader("User-Agent", "Finagle 4.0.2 - Liftweb")
    request.setHeader("Host", hostAndPort)

    val client = clientFactory.apply()()
    val response = client(request)
    response.onSuccess{res =>
      println("We saved the message %s".format(m))
    }

  }
}
