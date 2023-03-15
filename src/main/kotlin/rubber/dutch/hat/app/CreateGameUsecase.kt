package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.toGameResponse
import rubber.dutch.hat.domain.model.GameConfig
import rubber.dutch.hat.domain.service.GameCreator

@Component
class CreateGameUsecase(
    private val gameCreator: GameCreator
) {

    fun execute(payload: CreateGameRequestPayload): GameResponse {
        val game = gameCreator.createGame(
            payload.creatorId,
            GameConfig(
                wordsPerPlayer = payload.wordsPerPlayer,
                moveTime = payload.moveTime
            )
        )
        return game.toGameResponse()
    }

}
