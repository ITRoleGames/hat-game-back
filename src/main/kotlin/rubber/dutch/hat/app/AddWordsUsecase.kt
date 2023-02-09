package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.AddWordsRequestPayload
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.model.event.GameUpdatedEvent
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.domain.port.GameSaver
import rubber.dutch.hat.domain.service.GameProvider

@Component
class AddWordsUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver,
    private val eventSender: EventSender
) {

    fun execute(creatorId: UserId, payload: AddWordsRequestPayload) {
        val game = gameProvider.findById(payload.gameId) ?: throw GameNotFoundException()
        game.addWordsToPlayer(creatorId, payload.words)
        gameSaver.save(game)
        eventSender.send(GameUpdatedEvent(game.id.gameId))
    }
}
