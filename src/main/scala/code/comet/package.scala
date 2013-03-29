package code

package object comet {
  type InboxMessages =  Vector[ChatMessage]
  case object GetAll
  case class AddMessage(m: ChatMessage)
}


