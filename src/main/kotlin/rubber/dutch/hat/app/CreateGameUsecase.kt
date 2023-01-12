package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.GameDto
import rubber.dutch.hat.app.dto.toDto
import rubber.dutch.hat.domain.model.GameConfig
import rubber.dutch.hat.domain.service.GameCreator

@Component
class CreateGameUsecase(
  private val gameCreator: GameCreator
) {

  fun execute(payload: CreateGameRequestPayload): GameDto {
    val game = gameCreator.createGame(
      payload.creatorId,
      GameConfig(
        wordsPerPlayer = payload.wordsPerPlayer,
        moveTime = payload.moveTime
      )
    )
    return game.toDto()
  }

}