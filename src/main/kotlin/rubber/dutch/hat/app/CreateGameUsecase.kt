package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.CreateGameRequest
import rubber.dutch.hat.app.dto.CreateGameResponse
import rubber.dutch.hat.domain.model.GameConfig
import rubber.dutch.hat.domain.service.GameCreator

@Component
class CreateGameUsecase(
  private val gameCreator: GameCreator
) {

  fun execute(request: CreateGameRequest): CreateGameResponse {
    val game = gameCreator.createGame(
      request.creatorId,
      GameConfig(
        wordsPerPlayer = request.wordsPerPlayer,
        moveTime = request.moveTime
      )
    )
    return CreateGameResponse(
      id = game.gameId,
      code = game.code
    )
  }

}