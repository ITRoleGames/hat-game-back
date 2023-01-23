package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.UserId

data class JoinGameRequestPayload(

  @field:Schema(description = "код игры")
  val code: String,

  @field:Schema(description = "id пользователя")
  val userId: UserId
)
