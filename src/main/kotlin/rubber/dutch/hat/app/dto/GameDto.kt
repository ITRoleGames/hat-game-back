package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.Game
import java.util.*

data class GameDto(
  @field:Schema(description = "id игры")
  val id: UUID,

  @field:Schema(description = "код игры")
  val code: String,

  @field:Schema(description = "Количество слов на участника игры")
  val wordsPerPlayer: Int,

  @field:Schema(description = "Количество секунд на ход")
  val moveTime: Int
)

fun Game.toDto() : GameDto {
  return GameDto(
    id = gameId,
    code = code,
    wordsPerPlayer = config.wordsPerPlayer,
    moveTime = config.moveTime
  )
}