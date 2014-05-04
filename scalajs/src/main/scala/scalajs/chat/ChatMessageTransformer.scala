package scalajs.chat

import shared._
import org.scalajs.jquery._
import scala.scalajs.js
import scala.scalajs.js.JSON
import org.scalajs.dom
import org.scalajs.spickling._
import org.scalajs.spickling.jsany._

object ChatMessagesTransformer {

  PicklerRegistry.register[ChatMessage]
  PicklerRegistry.register[JoinChat]
  PicklerRegistry.register[ExitChat]

  def fromJson(_json: String): Option[ChatMessages] = {
    try {
      val json = jQuery.parseJSON(_json).asInstanceOf[js.Any]
      val message = PicklerRegistry.unpickle(json)
      Some(message.asInstanceOf[ChatMessages])
    } catch {
      case _: Throwable => {
        None
      }
    }
  }

  def asJson(message: ChatMessages): js.Any = {
    try {
      val jsObj = PicklerRegistry.pickle(message)
      val json = JSON.stringify(jsObj)
      json.replaceAllLiterally("$1", "")
    } catch {
      case _: Throwable =>
        dom.console.log(s"could not serialize message: $message")
    }
  }
}