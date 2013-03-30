package code

package object comet {
  type InboxMessages =  Vector[ChatMessage]
  case class GetAll(roomName: String)
  case class AddMessage(m: ChatMessage)
}


