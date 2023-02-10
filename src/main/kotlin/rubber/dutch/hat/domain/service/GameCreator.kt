package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.GameConfig
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.port.GameRepository
import java.util.*

@Component
class GameCreator(private val gameRepository: GameRepository) {

  fun createGame(creatorId: UserId, config: GameConfig): Game {
    val game = Game(
      id = GameId(UUID.randomUUID()),
      code = generateCode(),
      creatorId = creatorId,
      config = config,
    ).also {
      it.addPlayer(creatorId)
    }
    return gameRepository.save(game)
  }

  private fun generateCode(): String {
    return UUID.randomUUID().toString().replace("-", "")
  }

}
