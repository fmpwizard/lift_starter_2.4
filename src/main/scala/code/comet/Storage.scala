package code.comet

import net.liftweb.actor.LiftActor
import net.liftweb.common.Loggable

object Storage extends LiftActor with Loggable {

  private var msgs: Option[InboxMessages] = None

  override def messageHandler = {
    case AddMessage( m ) =>
      logger.info("got a message: %s" format m)
      addNewMessage( m )
      logger.info("Now we have %s messages" format msgs)
    case GetAll          =>
      logger.info("got a GetAll, will send %s" format msgs)
      reply( msgs )
  }


  private[this] def addNewMessage(message: ChatMessage) {
    val newMessages = msgs.map { m =>
      m ++ Vector( message )
    } getOrElse {
      Vector( message )
    }

    msgs = Some(newMessages)

  }
}
