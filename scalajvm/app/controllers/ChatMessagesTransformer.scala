package controllers

import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toContraFunctorOps
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import shared._
import play.api.libs.json.Writes

object ChatMessagesTransformer {

  import play.api.libs.json.Json

  implicit val chatMessageReads = Json.reads[ChatMessage]
  implicit val joinChatReads = Json.reads[JoinChat]
  implicit val exitChatReads = Json.reads[ExitChat]

  // Json.writes ignore 'discriminator' property
  implicit val chatWrites = new Writes[ChatMessages] {
    def writes(message: ChatMessages) = Json.obj(
      "discriminator" -> message.discriminator,
      message match {
        case JoinChat(user) => "user" -> user
        case ExitChat(user) => "user" -> user
        case ChatMessage(user, msg) => {
          "user" -> user
          "message" -> msg
        }
      })
  }
}