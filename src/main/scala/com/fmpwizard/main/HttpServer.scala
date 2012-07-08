package com.fmpwizard.main

import com.twitter.finagle.http.path._
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http._
import com.twitter.finagle.Service
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.twitter.util.{JavaTimer, Timer, Future}
import com.twitter.conversions.time._
import java.net.InetSocketAddress
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.stream.StreamResponse
import com.twitter.concurrent.{Offer, Broker}
import org.jboss.netty.buffer.ChannelBuffer
import path./
import scala.util.Random
import com.fmpwizard.actors.{RemoveMessage, GetMessages, InboxActor}
import com.twitter.finagle.http.path./
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.util.CharsetUtil
import com.twitter.finagle.http.RichHttp

/**
 * This example demonstrates a sophisticated HTTP server that handles exceptions
 * and performs authorization via a shared secret. The exception handling and
 * authorization code are written as Filters, thus isolating these aspects from
 * the main service (here called "Respond") for better code organization.
 */
object HttpServer {

  // "tee" messages across all of the registered brokers.
  val addBroker = new Broker[Broker[ChannelBuffer]]
  val remBroker = new Broker[Broker[ChannelBuffer]]
  val messages = new Broker[ChannelBuffer]

  private[this] def tee(receivers: Set[Broker[ChannelBuffer]]) {
    Offer.select(
      addBroker.recv {
        b => tee(receivers + b)
      },
      remBroker.recv {
        b => tee(receivers - b)
      },
      if (receivers.isEmpty) Offer.never
      else {
        messages.recv {
          m =>
            Future.join(receivers map {
              _ ! m
            } toSeq) ensure tee(receivers)
        }
      }
    )
  }

  private[this] def produce(r: Random, t: Timer) {
    t.schedule(1.second.fromNow) {
      val chatMsg = InboxActor !? GetMessages
      InboxActor ! RemoveMessage
      //val m = copiedBuffer(r.nextInt.toString + "\n", CharsetUtil.UTF_8)
      val m = copiedBuffer(chatMsg.toString + "\n", CharsetUtil.UTF_8)
      messages.send(m) andThen produce(r, t)
    }
  }

  // start the two processes.
  tee(Set())
  produce(new Random, new JavaTimer)



  /**
   * The service itself. Simply echos back "hello world"
   */
  class Respond extends Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = {
      val response = new DefaultHttpResponse(HTTP_1_1, OK)
      response.setContent(copiedBuffer("hello world", UTF_8))
      Future.value(response)
    }
  }


  /**
   * Handles the messages coming in to the service. (From the
   * other data centers
   */
  class MessagesIn extends Service[Request, Response] {
    def apply(request: Request) = {
      println("Got $$$$$" + request.content.toString(UTF_8))
      val response = new DefaultHttpResponse(HTTP_1_1, OK)
      response.setContent(copiedBuffer("search service world", UTF_8))
      Future.value(Response(response))
    }
  }

  /**
   * Streams the messages to all our Data centers.
   */
  class MessagesOut extends Service[HttpRequest, StreamResponse] {
    def apply(request: HttpRequest) = Future {
      val subscriber = new Broker[ChannelBuffer]
      addBroker ! subscriber
      new StreamResponse {
        val httpResponse = new DefaultHttpResponse(
          request.getProtocolVersion, HttpResponseStatus.OK)

        def messages = subscriber.recv

        def error = new Broker[Throwable].recv

        def release() = {
          remBroker ! subscriber
          // sink any existing messages, so they
          // don't hold up the upstream.
          subscriber.recv foreach {
            _ => ()
          }
        }
      }
    }
  }



  val myMessagesIn = new MessagesIn
  val myMessagesOut = new MessagesOut


  def main(args: Array[String]) {
    val respond = new Respond

    val routingService =
      MyRoutingService.byRequest { request =>
        (request.method, Path(request.path)) match {
          case Method.Post -> Root / "messages"  => myMessagesIn
          case Method.Get  -> Root / "messages"  => myMessagesOut
        }
      }

    // compose the Filters and Service together:
    //val myService: Service[HttpRequest, HttpResponse] = handleExceptions andThen respond

    val server: Server = ServerBuilder()
      .codec(RichHttp[Request](Http()))
      .bindTo(new InetSocketAddress(8080))
      .name("httpserver")
      .build(routingService)
  }
}

object MyRoutingService {
  def byRequest[REQUEST](routes: PartialFunction[Request, Service[REQUEST, Response]]) =
    new RoutingService(
      new PartialFunction[Request, Service[REQUEST, Response]] {
        def apply(request: Request)       = routes(request)
        def isDefinedAt(request: Request) = routes.isDefinedAt(request)
      }
    )
}