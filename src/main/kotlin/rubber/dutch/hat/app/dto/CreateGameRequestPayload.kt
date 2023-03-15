package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.UserId

data class CreateGameRequestPayload(

    @field:Schema(description = "id пользователя создающего игру")
    val creatorId: UserId,

    @field:Schema(description = "Количество слов на участника игры")
    val wordsPerPlayer: Int,

    @field:Schema(description = "Количество секунд на ход")
    val moveTime: Int
)
