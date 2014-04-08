package shared

sealed trait ChatMessages {
  val clazz = {
    val name = this.getClass.getName
    name.substring(name.lastIndexOf(".") + 1)
  }
}

case class ChatMessage(user: String, message: String) extends ChatMessages

case class JoinChat(user: String) extends ChatMessages

case class ExitChat(user: String) extends ChatMessages