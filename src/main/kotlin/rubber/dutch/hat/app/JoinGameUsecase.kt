package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.app.dto.toGameResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameUserManager

@Component
class JoinGameUsecase(
  private val gameProvider: GameProvider,
  private val gameUserManager: GameUserManager
) {

  fun execute(payload: JoinGameRequestPayload): GameResponse {
    val game = gameProvider.findByCode(payload.code) ?: throw GameNotFoundException()
    if (!game.isUserJoined(payload.userId)) {
      gameUserManager.joinUserToGame(payload.userId, game)
    }
    return game.toGameResponse()
  }

}