package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class CreateGameResponse(
  @field:Schema(description = "id игры")
  val id: UUID,

  @field:Schema(description = "код игры")
  val code: String,
)