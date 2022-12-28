package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.CreateGameResponse
import rubber.dutch.hat.domain.model.GameConfig
import rubber.dutch.hat.domain.service.GameCreator

@Component
class CreateGameUsecase(
  private val gameCreator: GameCreator
) {

  fun execute(payload: CreateGameRequestPayload): CreateGameResponse {
    val game = gameCreator.createGame(
      payload.creatorId,
      GameConfig(
        wordsPerPlayer = payload.wordsPerPlayer,
        moveTime = payload.moveTime
      )
    )
    return CreateGameResponse(
      id = game.gameId,
      code = game.code
    )
  }

}