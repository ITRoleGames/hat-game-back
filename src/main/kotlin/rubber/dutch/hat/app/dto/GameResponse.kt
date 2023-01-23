package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.GameId

data class GameResponse(
  @field:Schema(description = "id игры")
  val id: GameId,

  @field:Schema(description = "код игры")
  val code: String,

  @field:Schema(description = "Количество слов на участника игры")
  val wordsPerPlayer: Int,

  @field:Schema(description = "Количество секунд на ход")
  val moveTime: Int
)

fun Game.toGameResponse() : GameResponse {
  return GameResponse(
    id = id,
    code = code,
    wordsPerPlayer = config.wordsPerPlayer,
    moveTime = config.moveTime
  )
}