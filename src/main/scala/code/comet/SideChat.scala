package code.comet

import net.liftweb.http.CometActor
import code.snippet.{CurrentUser, UserLoggedIn}
import net.liftweb.common._
import scala.xml.NodeSeq
import net.liftweb.actor.LAFuture
import net.liftweb.util.ClearClearable


class SideChat extends CometActor with Loggable {

  override def lowPriority = {
    case message: ChatMessage =>
      partialUpdate( SideSingleChatMessage( message ) )

    case InitialRender =>
      logger.debug("Initial render")
      logger.debug("Will request %s" format name.openOr("public") )
      val futureStoredMessages = Storage !< GetAll( name.openOr("public") )
      partialUpdate( UserLoggedIn(CurrentUser.is) )
      sendStoredMessages( futureStoredMessages )

    case x => logger.debug("got: %s as a message " format x)
  }

  def render = {
    "h2 *+"                               #> name &
      "#message-bind  [data-ng-bind]"       #> "todo.message" &
      "#timestamp     [title]"              #> "{{todo.createdAt}}" &
      "#username      [data-ng-bind]"       #> "todo.username" &
      "#message-id    [data-ng-bind]"       #> "todo.id" &
      "#messages-repeater [data-ng-repeat]" #> "todo in todos" &
      ClearClearable
  }




  override def fixedRender: Box[NodeSeq] = {
    this ! InitialRender
    logger.debug("Calling Initial render")
    NodeSeq.Empty
  }

  private[this] def sendStoredMessages(futureStoredMessages: LAFuture[Any]) {
    futureStoredMessages.foreach { storedMessages =>
      for {
        maybeMessages  <- Box.asA[Option[InboxMessages]](storedMessages)
        messages       <- maybeMessages
      } yield {
        logger.debug("made it %s" format messages)
        partialUpdate( SideStoredChatMessage( messages ) )
      }
    }
  }
}

case class SideSingleChatMessage(data: ChatMessage) extends ChatRoomEvent("new-side-chat-message")
case class SideStoredChatMessage(data: InboxMessages) extends ChatRoomEvent("initial-side-chat-messages")
