package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.GameConfig
import rubber.dutch.hat.domain.port.GameSaver
import java.util.*

@Component
class GameCreator(private val gameSaver: GameSaver) {

  fun createGame(creatorId: String, config: GameConfig): Game {
    val game = Game(
      gameId = generateUuid(),
      code = generateUuid(),
      creatorId = creatorId,
      config = config,
      users = mutableSetOf(creatorId)
    )
    return gameSaver.save(game)
  }

  private fun generateUuid(): String {
    return UUID.randomUUID().toString().replace("-", "")
  }

}