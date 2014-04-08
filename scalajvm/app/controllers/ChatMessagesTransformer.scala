package controllers

import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toContraFunctorOps
import play.api.libs.functional.syntax.toFunctionalBuilderOps

import shared._

object ChatMessagesTransformer {

  import play.api.libs.json.Json

  implicit val chatMessageReads = Json.reads[ChatMessage]
  implicit val chatMessageWrites = Json.writes[ChatMessage]
  
  implicit val joinChatReads = Json.reads[JoinChat]
  implicit val joinChatWrites = Json.writes[JoinChat]
  
  implicit val exitChatReads = Json.reads[ExitChat]
  implicit val exitChatWrites = Json.writes[ExitChat]

}