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
import controllers.{ ChatMessagesTransformer => transformer }
import shared._
import play.api.libs.json.OWrites

object Application extends Controller {

  def index = Action {
    Ok(views.html.chat())
  }

  var channels = Set[Channel[String]]()

  //This shows an updated websocket example for play 2.2.0 utilizing Concurrent.broadcast vs Enumerator.imperative, which is now deprecated.
  def chat(name: String) = WebSocket.using[String] { request =>

   	println(s"$name connected") 
   	notifyAll(JoinChat(name))

    val (out, channel) = Concurrent.broadcast[String]
    channels += channel

    val in = Iteratee.foreach[String](handleIncommingMessage) map { _ =>
      println(s"$name disconnected")
      notifyAll(ExitChat(name))
      channels -= channel
    }    

    (in, out)
  }

  private def handleIncommingMessage = (message: String) => {
    println(s"received $message")
    
    transformer.fromJson(message) map (_ => notifyAll(message)) getOrElse {
      println(s"could not parse $message")
    }
  }

  private def notifyAll(json: String) {
    channels foreach (_ push json)
  }

  private def notifyAll(message: ChatMessages) {
    val json = transformer.asJson(message)
    channels foreach (_ push json)
  }

}