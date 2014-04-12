package scalajs.chat

import shared._
import org.scalajs.jquery._
import scala.scalajs.js
import scala.scalajs.js.JSON

object ChatMessageTransformer {

  def fromJson(_json: String): Option[ChatMessages] = {
    val json = jQuery.parseJSON(_json)
    val clazz = json.clazz.asInstanceOf[String]
    clazz match {
      case ChatMessage.discriminator => Some(ChatMessage(json.user.asInstanceOf[String], json.message.asInstanceOf[String]))
      case JoinChat.discriminator => Some(JoinChat(json.user.asInstanceOf[String]))
      case ExitChat.discriminator => Some(ExitChat(json.user.asInstanceOf[String]))
      case _ => None
    }
  }
  
  def asJson(message: ChatMessages): String = {
    val jsObj = message.asInstanceOf[js.Any]
    val json = JSON.stringify(jsObj)
    json.replaceAllLiterally("$1", "")
  }
}