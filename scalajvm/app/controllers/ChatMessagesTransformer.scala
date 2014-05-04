package controllers

import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toContraFunctorOps
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import shared._
import org.scalajs.spickling.playjson._
import org.scalajs.spickling.PicklerRegistry
import play.api.libs.json.Json

object ChatMessagesTransformer {

  PicklerRegistry.register[ChatMessage]
  PicklerRegistry.register[JoinChat]
  PicklerRegistry.register[ExitChat]

  def fromJson(_json: String): Option[ChatMessages] = {
    try {
      val json = Json.parse(_json)
      PicklerRegistry.unpickle(json) match {
        case message: ChatMessages => Some(message)
        case _ => None
      }
    } catch {
      case _: Throwable => {
        None
      }
    }
  }

  def asJson(message: ChatMessages): String = {
    try {
      val jsObj = PicklerRegistry.pickle(message)
      Json.stringify(jsObj)
    } catch {
      case cause: Throwable =>
        println(s"could not serialize message: $message")
        throw cause
    }
  }
}