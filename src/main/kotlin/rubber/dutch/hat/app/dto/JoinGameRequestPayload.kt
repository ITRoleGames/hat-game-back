package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class JoinGameRequestPayload(

  @field:Schema(description = "код игры")
  val code: String,

  @field:Schema(description = "id пользователя")
  val userId: UUID
)
