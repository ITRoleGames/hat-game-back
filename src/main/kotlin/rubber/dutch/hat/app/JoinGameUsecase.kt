package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameDto
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.app.dto.toDto
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.model.event.GameUpdatedEvent
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameUserManager

@Component
class JoinGameUsecase(
        private val gameProvider: GameProvider,
        private val gameUserManager: GameUserManager,
        private val eventSender: EventSender
) {

    fun execute(payload: JoinGameRequestPayload): GameDto {
        val game = gameProvider.findByCode(payload.code) ?: throw GameNotFoundException()
        if (!game.userIsJoined(payload.userId)) {
            gameUserManager.joinUserToGame(payload.userId, game)
        }

        eventSender.send(GameUpdatedEvent(game.gameId))

        return game.toDto()
    }

}