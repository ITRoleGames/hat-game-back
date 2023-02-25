package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.*

data class GameResponse(
        @field:Schema(description = "Id игры")
        val id: GameId,

        @field:Schema(description = "Код игры")
        val code: String,

        @field:Schema(description = "Создатель игры")
        val creatorId: String,

        @field:Schema(description = "Количество слов на участника игры")
        val wordsPerPlayer: Int,

        @field:Schema(description = "Количество секунд на ход")
        val moveTime: Int,

        @field:Schema(description = "Игроки")
        val players: List<PlayerDto>,

        @field:Schema(description = "Количество слов")
        val wordsCount: Int,

        @field:Schema(description = "Статус игры")
        val status: Game.GameStatus
)

fun Game.toGameResponse(): GameResponse {
    return GameResponse(
        id = id,
        code = code,
        creatorId = creatorId.toString(),
        wordsPerPlayer = config.wordsPerPlayer,
        moveTime = config.moveTime,
        players = players.map(Player::toDto),
        wordsCount = words.size,
        status = status
    )
}
