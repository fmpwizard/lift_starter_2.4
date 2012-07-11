package com.fmpwizard.stream

import net.liftweb.common.{Empty, Full, Box}
import java.io.{OutputStream, ByteArrayInputStream}
import net.liftweb.http.{InMemoryResponse, OutputStreamResponse, LiftResponse}
import com.fmpwizard.actors._
import org.jboss.netty.buffer.ChannelBuffers._
import org.jboss.netty.util.CharsetUtil
import com.twitter.util.{JavaTimer, Timer, Future}
import com.twitter.conversions.time._



object LiftStream {


  def send: Box[LiftResponse] = {
      Full(buildResponse)
  }

  //def receive(m: Box[Array[Byte]]): Box[LiftResponse] = {
  def receive(m: Map[String, List[String]]): Box[LiftResponse] = {
    val userName = m.get("username")
    val message = m.get("message")
    for {
      u <- userName
      m <- message
    } yield InboxActor ! AddMessages(u.headOption, m.headOption)

    val res = InMemoryResponse(("Got : " + m + "\n").getBytes(CharsetUtil.UTF_8), ("Content-Type", "application.json") :: Nil, Nil, 200 )
    Full(res)
  }

  private[this] def myStreamFunc(baos:OutputStream): Unit = {
    val chatMsg = InboxActor !? GetMessages
    val l = Box.asA[List[(Box[String], Box[String])]](chatMsg)
    InboxActor ! RemoveMessages(l.openOr(List((Empty,Empty))))
    val m = copiedBuffer(chatMsg.toString + "\n", CharsetUtil.UTF_8)
    baos.write(chatMsg.toString.getBytes(CharsetUtil.UTF_8))
  }

  private[this] def buildResponse = {
    OutputStreamResponse(myStreamFunc)
  }
}
