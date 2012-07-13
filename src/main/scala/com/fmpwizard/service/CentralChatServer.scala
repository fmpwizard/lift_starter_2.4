package com.fmpwizard
package service

import actors._
import com.twitter.finagle.ServiceFactory
import org.jboss.netty.handler.codec.http._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Http
import com.twitter.util.TimeConversions._
import actors._
import net.liftweb.util._
import org.jboss.netty.buffer.ChannelBuffers
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import actors._
import org.jboss.netty.util.CharsetUtil._
import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.util.CharsetUtil
import org.joda.time.DateTime
import net.liftweb.common.Logger
import org.joda.time.format.DateTimeFormat


object CentralChatServer extends Logger{

  private[this] val hostAndPort = "fmpwizard.iriscouch.com:6984"

  /**
   * You init a clientFactory only once and use it several times across your application
   */
  private[this] val clientFactory: ServiceFactory[HttpRequest, HttpResponse] = ClientBuilder()
    .codec(Http())
    .tls("fmpwizard.iriscouch.com")
    .hosts(hostAndPort)
    .tcpConnectTimeout(1.second)
    .hostConnectionLimit(1)
    .buildFactory()

  /**
   * Send our payload to the CouchDB server, and then the other datacenters can
   * read the new entry and broadcast the new message.
   */
  def sendToCouchDB(m: Message) ={

    val j = ("user" -> m.user) ~
      ("msg" -> m.msg) ~
      ("datetime" -> m.dateTime.toString("YYY MMM dd hh:mm:ss ZZ")) ~
      ("host" -> java.net.InetAddress.getLocalHost.getHostName)

    val path = "/chatserver/%s".format(Helpers.nextFuncName)
    val payload = ChannelBuffers.copiedBuffer( compact(render(j))  , UTF_8)
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, path)
    request.setHeader("User-Agent", "Finagle 4.0.2 - Liftweb")
    request.setHeader("Host", hostAndPort)
    request.setHeader(CONTENT_TYPE, "application/json")
    request.setHeader(CONNECTION, "keep-alive")
    request.setHeader(CONTENT_LENGTH, String.valueOf(payload.readableBytes()));
    request.setContent(payload)

    val client = clientFactory.apply()()
    val response = client(request)
    response.onSuccess{res =>
      info("We saved the message %s".format(m))
      client.release()
    }.onFailure(err =>
      error("Cound not send data to couch from sendToCouchDB %s" format err.getStackTrace)
    )
  }

  def readChangesFeed(since: BigInt = 0): Unit = {
    val path = "/chatserver/_changes?include_docs=true&since=%s".format(since)
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path)
    request.setHeader("User-Agent", "Finagle 4.0.2 - Liftweb")
    request.setHeader("Host", hostAndPort)

    val client = clientFactory.apply()()
    val response = client(request)
    response.onSuccess{res =>
      val json = parse(res.getContent.toString(CharsetUtil.UTF_8))
      val shorter: List[MessageRow] = for {
        JObject(child) <- json
        JField("user", JString(username))    <- child
        JField("msg" , JString(message))     <- child
        JField("datetime",JString(datetime)) <- child
        JField("host",JString(hostname))     <- child
      } yield {
        val fmt = DateTimeFormat.forPattern("YYY MMM dd hh:mm:ss ZZ")
        MessageRow(fmt.parseDateTime(datetime), username, message,hostname)
      }


      debug("json is %s".format(json))
      info("res is %s".format(res.getContent.toString(CharsetUtil.UTF_8)))
      debug("shorter size is %s".format(shorter.size))
      shorter.map(msg => {
        InboxActor ! msg
      })

      for {
        JField("last_seq", JInt(since))    <- json
      } yield InboxActor ! Since(since)
      client.release()

    }.onFailure{
      err =>{
        error("Cound not read data from couch from readChangesFeed %s %s %s".format( err.getCause, err.getMessage, err.getStackTraceString ))
        InboxActor ! Since(0)
        client.release()
      }
    }

  }
}
