package shared

sealed trait ChatMessages 

case class ChatMessage(user: String, message: String) extends ChatMessages 

case class JoinChat(user: String) extends ChatMessages 

case class ExitChat(user: String) extends ChatMessages