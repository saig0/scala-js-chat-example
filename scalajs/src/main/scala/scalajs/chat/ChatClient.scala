package scalajs.chat

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.jquery._
import org.scalajs.dom.WebSocket
import org.scalajs.dom.MessageEvent
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.Any.fromString
import scala.scalajs.js.Any.stringOps
import shared._

@JSExport
object ChatClient {

  private def asJson(obj: Any): String = {
    val jsObj = obj.asInstanceOf[js.Any]
    val json = JSON.stringify(jsObj)
    json.replaceAllLiterally("$1", "")
  }

  private def parseChatMessage(_json: String): Option[ChatMessages] = {
    val json = jQuery.parseJSON(_json)
    val clazz = json.clazz.asInstanceOf[String]
    clazz match {
      case "ChatMessage" => Some(ChatMessage(json.user.asInstanceOf[String], json.message.asInstanceOf[String]))
      case "JoinChat" => Some(JoinChat(json.user.asInstanceOf[String]))
      case "ExitChat" => Some(ExitChat(json.user.asInstanceOf[String]))
      case _ => None
    }
  }

  @JSExport
  def main(args: Array[String]) {
     val url = dom.location.host

    val ws = new WebSocket(s"ws://$url/chat")
    ws.onmessage = onMessage

    jQuery("#chat").hide

    jQuery("#login-button").click(() => {
      ws.send(asJson(JoinChat(user)))

      jQuery("#login").hide
      jQuery("#chat").show
      jQuery("#message").focus
    })

    jQuery("#message-button").click(() => {
      ws.send(asJson(ChatMessage(user, message)))
      jQuery("#message").value("")
      jQuery("#message").focus
    })

  }

  private def user = jQuery("#user").value.toString
  private def message = jQuery("#message").value.toString

  private def onMessage: (MessageEvent) => Unit = (message: MessageEvent) => {
    val json = message.data.toString
    val msg = parseChatMessage(json) map (_ match {
      case JoinChat(user) => s"$user joined"
      case ChatMessage(user, message) => s"$user > $message"
      case ExitChat(user) => s"$user left"
    }) getOrElse {
      dom.console.log(s"could not parse message: $json")
    }

    val p = jQuery("<p>").html(s"$msg")
    jQuery("#messages").append(p)
    jQuery("#messages").scrollTop(jQuery("#messages").height)
  }
}