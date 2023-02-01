package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.GameConfigProperties
import rubber.dutch.hat.domain.exception.PlayersLimitExceededException
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.port.GameSaver

@Component
class GameUserManager(
  private val gameSaver: GameSaver,
  private val gameConfigProperties: GameConfigProperties
) {

  fun joinUserToGame(userId: UserId, game: Game) {
    if (game.players.size < gameConfigProperties.maxPlayers) {
      game.addPlayer(userId)
      gameSaver.save(game)
    } else {
      throw PlayersLimitExceededException()
    }
  }

}
