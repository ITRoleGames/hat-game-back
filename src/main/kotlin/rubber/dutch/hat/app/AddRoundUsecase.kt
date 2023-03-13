package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.RoundDto
import rubber.dutch.hat.app.dto.toDto
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.exception.PlayerNotFoundException
import rubber.dutch.hat.domain.exception.UserNotJoinedException
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.GameStatus
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.model.event.GameUpdatedEvent
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameSaver
import rubber.dutch.hat.domain.service.PlayerProvider
import rubber.dutch.hat.domain.service.RoundSaver

@Component
class AddRoundUsecase(
    private val gameProvider: GameProvider,
    private val playerProvider: PlayerProvider,
    private val roundSaver: RoundSaver,
    private val eventSender: EventSender
) {
    fun execute(gameId: GameId, userId: UserId): RoundDto {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        if (game.status != GameStatus.STARTED) {
            throw GameStatusException()
        }

        if (!game.isUserInGame(userId)) {
            throw UserNotJoinedException()
        }

        val player = playerProvider.findByUserId(userId) ?: throw PlayerNotFoundException()

        val round = game.addNewRound(player.id, game.id)
        game.addNewExplanation(round.id)
        val savedRound = roundSaver.save(round)

        eventSender.send(GameUpdatedEvent(game.id))
        return savedRound.toDto()
    }
}
