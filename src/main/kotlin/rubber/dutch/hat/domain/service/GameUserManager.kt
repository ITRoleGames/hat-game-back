package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.GameConfigProperties
import rubber.dutch.hat.domain.exception.PlayersLimitExceededException
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.port.GameSaver
import java.util.*

@Component
class GameUserManager(
  private val gameSaver: GameSaver,
  private val gameConfigProperties: GameConfigProperties
) {

  fun joinUserToGame(userId: UUID, game: Game) {
    if (game.users.size < gameConfigProperties.maxPlayers) {
      game.users.add(userId)
      gameSaver.save(game)
    } else {
      throw PlayersLimitExceededException()
    }
  }

}