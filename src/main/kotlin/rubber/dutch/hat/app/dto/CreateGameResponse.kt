package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CreateGameResponse(
  @field:Schema(description = "id игры")
  val id: String,

  @field:Schema(description = "код игры")
  val code: String,
)