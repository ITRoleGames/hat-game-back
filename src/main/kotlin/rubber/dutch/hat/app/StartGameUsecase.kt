package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.toGameResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.exception.OperationNotPermittedException
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameSaver

@Component
class StartGameUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver,
) {

    fun execute(gameId: GameId, currentUserId: UserId): GameResponse {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        if (game.status != Game.GameStatus.NEW) throw GameStatusException()
        if (game.creatorId != currentUserId) throw OperationNotPermittedException()

        val oddPlayerList: List<Player>
        var lastPlayer: Player? = null

        if (game.players.size % DEFAULT_TEAM_SIZE == 0) {
            oddPlayerList = game.players
        } else {
            oddPlayerList = game.players.take(game.players.size - 1)
            lastPlayer = game.players.last()
        }

        var teamId = 0
        var moveOrder = 0
        oddPlayerList.shuffled().chunked(DEFAULT_TEAM_SIZE).forEach {
            it[0].teamId = teamId
            it[0].moveOrder = moveOrder
            it[1].teamId = teamId
            it[1].moveOrder = moveOrder + oddPlayerList.size / 2
            teamId++
            moveOrder++
        }

        if (lastPlayer != null) {
            lastPlayer.teamId = 0
            moveOrder = oddPlayerList.filter { it.teamId == 0 }.maxBy { it.moveOrder }.moveOrder
            lastPlayer.moveOrder = moveOrder
        }

        game.status = Game.GameStatus.STARTED
        return gameSaver.saveAndNotify(game, currentUserId).toGameResponse()
    }

    companion object {
        private const val DEFAULT_TEAM_SIZE = 2
    }
}
