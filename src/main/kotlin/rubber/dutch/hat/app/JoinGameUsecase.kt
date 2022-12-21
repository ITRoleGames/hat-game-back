package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.JoinGameRequest
import rubber.dutch.hat.app.dto.JoinGameResponse
import rubber.dutch.hat.domain.GameConfigProperties
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameUserManager

@Component
class JoinGameUsecase(
  private val gameProvider: GameProvider,
  private val gameUserManager: GameUserManager
) {

  fun execute(request: JoinGameRequest): JoinGameResponse {
    val game = gameProvider.findByCode(request.code) ?: throw GameNotFoundException()
    if (!game.userIsJoined(request.userId)) {
      gameUserManager.joinUserToGame(request.userId, game)
    }
    return JoinGameResponse()
  }

}