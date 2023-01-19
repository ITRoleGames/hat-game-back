package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.GameConfig
import rubber.dutch.hat.domain.port.GameSaver
import java.util.*

@Component
class GameCreator(private val gameSaver: GameSaver) {

  fun createGame(creatorId: UUID, config: GameConfig): Game {
    val game = Game(
      gameId = generateUuid(),
      code = generateCode(),
      creatorId = creatorId,
      config = config,
    )
    val newGame = gameSaver.save(game)
    // TODO: получается очень коряво с этими двумя сохранениями. Перейти на UUID как первичный ключ?
    newGame.addPlayer(creatorId)
    return gameSaver.save(newGame)
  }

  private fun generateUuid(): UUID {
    return UUID.randomUUID()
  }

  private fun generateCode(): String {
    return UUID.randomUUID().toString().replace("-", "")
  }

}