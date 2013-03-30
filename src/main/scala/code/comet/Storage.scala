package code.comet

import net.liftweb.actor.LiftActor
import net.liftweb.common.Loggable
import collection.mutable

object Storage extends LiftActor with Loggable {

  private val msgs: mutable.Map[String, Option[InboxMessages]] = mutable.Map()

  override def messageHandler = {
    case AddMessage( m ) =>
      logger.info("got a message: %s" format m)
      addNewMessage( m )
      logger.info("Now we have %s messages" format msgs)
    case GetAll(room)          =>
      val ret = msgs.lift.apply(room).flatten
      logger.info("got a GetAll(%s), will send %s".format(room, ret))
      reply( ret )
  }


  private[this] def addNewMessage(message: ChatMessage) {

    val newMessages: InboxMessages = msgs.lift(message.roomName).flatMap{ m =>
      m.map{ _messages =>
        _messages ++ Vector( message )
      }
    } getOrElse {
      Vector( message )
    }

    msgs(message.roomName) = Some(newMessages)

  }
}
