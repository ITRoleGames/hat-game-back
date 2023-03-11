package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.app.dto.toGameResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameSaver

@Component
class JoinGameUsecase(
        private val gameProvider: GameProvider,
        private val gameSaver: GameSaver
) {

  fun execute(payload: JoinGameRequestPayload): GameResponse {
    val game = gameProvider.findByCode(payload.code) ?: throw GameNotFoundException()
    game.addPlayer(payload.userId)
    gameSaver.saveAndNotify(game, payload.userId)
    return game.toGameResponse()
  }

}
