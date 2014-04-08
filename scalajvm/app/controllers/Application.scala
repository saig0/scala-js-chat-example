package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.Concurrent._
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import shared.JoinChat
import shared.ExitChat
import controllers.ChatMessagesTransformer._
import shared._

object Application extends Controller {

  def index = Action {
    Ok(views.html.chat())
  }

  var channels = Set[Channel[String]]()

  //This shows an updated websocket example for play 2.2.0 utilizing Concurrent.broadcast vs Enumerator.imperative, which is now deprecated.
  def chat = WebSocket.using[String] { request =>

    println("connected")

    //Concurrent.broadcast returns (Enumerator, Concurrent.Channel)
    val (out, channel) = Concurrent.broadcast[String]

    channels += channel

    val in = Iteratee.foreach[String](handleIncommingMessage) map { _ =>
      println("disconnected")
      channels -= channel
    }

    (in, out)
  }

  //log the message to stdout and send response back to client
  private def handleIncommingMessage = (message: String) => {
    println(message)

    parseChatMessage(message) map (_ => notifyAll(message)) getOrElse {
      println(s"could not parse $message")
    }

    //the Enumerator returned by Concurrent.broadcast subscribes to the channel and will 
    //receive the pushed messages
  }

  private def notifyAll(json: String) = channels foreach (_ push json)

  // TODO
  private def parseChatMessage(json: String): Option[ChatMessages] = {
    val msg = Json.parse(json)
    Json.fromJson[JoinChat](msg) map (Some(_)) getOrElse {
      Json.fromJson[ChatMessage](msg) map (Some(_)) getOrElse {
        Json.fromJson[ExitChat](msg) map (Some(_)) getOrElse
          None
      }
    }
  }

}