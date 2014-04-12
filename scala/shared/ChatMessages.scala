package shared

sealed trait ChatMessages {
  val discriminator: String
}

case class ChatMessage(user: String, message: String) extends ChatMessages {
  val discriminator = ChatMessage.discriminator
}

object ChatMessage {
  val discriminator = "ChatMessage"
}

case class JoinChat(user: String) extends ChatMessages {
  val discriminator = JoinChat.discriminator
}

object JoinChat {
  val discriminator = "JoinChat"
}

case class ExitChat(user: String) extends ChatMessages {
  val discriminator = ExitChat.discriminator
}

object ExitChat {
  val discriminator = "ExitChat"
}