package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.toGameResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.service.GameProvider

@Component
class GetGameUsecase(private val gameProvider: GameProvider) {

    fun execute(gameId: GameId, currentUserId: UserId): GameResponse {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        game.players.firstOrNull() { it.userId == currentUserId } ?: throw GameNotFoundException()

        return game.toGameResponse()
    }
}
