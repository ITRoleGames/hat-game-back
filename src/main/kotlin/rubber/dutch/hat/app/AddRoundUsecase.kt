package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.RoundResponseDto
import rubber.dutch.hat.app.dto.toDto
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.exception.UserNotJoinedException
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.GameStatus
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameSaver
import rubber.dutch.hat.domain.service.PlayerProvider

@Component
class AddRoundUsecase(
    private val gameProvider: GameProvider,
    private val playerProvider: PlayerProvider,
    private val gameSaver: GameSaver
) {
    fun execute(gameId: GameId, userId: UserId): RoundResponseDto {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        if (game.status != GameStatus.STARTED) {
            throw GameStatusException()
        }
        if (!game.isUserInGame(userId)) {
            throw UserNotJoinedException()
        }

        val player = playerProvider.finByUserId(userId)

        game.addNewRound(player!!.id, game.id)

        gameSaver.saveAndNotify(game)
        return (game.getLastRound() ?: throw RuntimeException()).toDto()
    }
}
