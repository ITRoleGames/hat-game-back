package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.AddWordsRequestPayload
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameSaver

@Component
class AddWordsUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver
) {

    fun execute(creatorId: UserId, payload: AddWordsRequestPayload) {
        val game = gameProvider.findById(payload.gameId) ?: throw GameNotFoundException()
        game.addWordsToPlayer(creatorId, payload.words)
        gameSaver.saveAndNotify(game, creatorId)
    }
}
