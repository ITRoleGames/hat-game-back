package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.*

data class GameResponse(
        @field:Schema(description = "id игры")
        val id: GameId,

        @field:Schema(description = "код игры")
        val code: String,

        @field:Schema(description = "Количество слов на участника игры")
        val wordsPerPlayer: Int,

        @field:Schema(description = "Количество секунд на ход")
        val moveTime: Int,

        @field:Schema(description = "Игроки")
        val players: List<PlayerDto>,

        @field:Schema(description = "Идентификатов следующего игрока")
        val nextPlayerId: Long,

        @field:Schema(description = "Команды")
        val teams: List<TeamDto>,

        @field:Schema(description = "Слова")
        val words: List<WordDto>
)

fun Game.toGameResponse(): GameResponse {
    return GameResponse(
        id = id,
        code = code,
        wordsPerPlayer = config.wordsPerPlayer,
        moveTime = config.moveTime,
        players = players.map(Player::toDto),
        nextPlayerId = nextPlayerId ?: -1,
        teams = makeTeams(players),
        words = words.map(WordInGame::toDto)
    )
}

fun makeTeams(player: MutableList<Player>): List<TeamDto> {
    return player.map{
        val currentTeamId = it.teamId
        TeamDto(id = it.teamId,
            teamNumber = it.teamId,
            userIds = player
                .filter{ it.teamId == currentTeamId }
                .map{ it.userId }
        )
    }
}
