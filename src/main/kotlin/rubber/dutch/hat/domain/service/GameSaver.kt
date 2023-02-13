package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.event.GameUpdatedEvent
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.domain.port.GameRepository

@Component
class GameSaver(private val gameRepository: GameRepository, private val eventSender: EventSender) {

    fun saveAndNotify(game: Game): Game {
        return gameRepository.save(game).also {
            eventSender.send(GameUpdatedEvent(game.id.gameId))
        }
    }
}
