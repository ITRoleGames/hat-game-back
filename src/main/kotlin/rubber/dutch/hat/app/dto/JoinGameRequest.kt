package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema

data class JoinGameRequest(

  @field:Schema(description = "код игры")
  val code: String,

  @field:Schema(description = "id пользователя")
  val userId: String
)
