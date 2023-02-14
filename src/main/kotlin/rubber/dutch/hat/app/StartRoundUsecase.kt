package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.RoundResponseDto
import rubber.dutch.hat.app.dto.toDto
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.exception.UserNotJoinedException
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.GameStatus
import rubber.dutch.hat.domain.model.PlayerInternalId
import rubber.dutch.hat.domain.model.event.GameUpdatedEvent
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.RoundCreator
import java.util.*

@Component
class StartRoundUsecase(
    private val roundCreator: RoundCreator,
    private val gameProvider: GameProvider,
    private val eventSender: EventSender
) {
    fun execute(gameId: UUID, playerId: Long): RoundResponseDto {
        val game = gameProvider.findById(GameId(gameId)) ?: throw GameNotFoundException()
        if (game.status != GameStatus.STARTED) {
            throw GameStatusException()
        }
        if (!game.players.any { it.id == PlayerInternalId(playerId) }) {
            throw UserNotJoinedException()
        }
        val round = roundCreator.createRound(GameId(gameId), PlayerInternalId(playerId))
        eventSender.send(GameUpdatedEvent(GameId(gameId)))
        return round.toDto()
    }
}
