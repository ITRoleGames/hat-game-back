package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.*
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
      status = Game.GameStatus.NEW
    ).also {
      it.addPlayer(creatorId)
    }
    return gameRepository.save(game)
  }

    private fun generateCode(): String {
        return ('a'..'z').map { it }.shuffled().subList(0, 3)
            .plus(
                (0..9).map { it.digitToChar() }.shuffled().subList(0, 2)
            )
            .shuffled()
            .joinToString("")
    }
}
