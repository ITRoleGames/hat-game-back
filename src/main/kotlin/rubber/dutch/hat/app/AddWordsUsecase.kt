package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.AddWordsRequestPayload
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.service.GameProvider
import java.util.*

@Component
class AddWordsUsecase(
  val gameProvider: GameProvider
) {

  fun execute(creatorId: UUID, payload: AddWordsRequestPayload) {
    val game = gameProvider.findById(payload.gameId) ?: throw GameNotFoundException()
    game.addWordsToPlayer(creatorId, payload.words)
  }

}