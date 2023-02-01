package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.toGameResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.port.GameSaver
import rubber.dutch.hat.domain.service.GameProvider
import java.util.*

@Component
class StartGameUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver,
) {

    fun execute(gameId: GameId): GameResponse {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()

        var counter = 2
        var teamId = 1;
        game.players.shuffled().withIndex().forEach {
            if (counter > 0) {
                it.value.teamId = teamId.toString()
                counter--
            } else {
                if (it.index < game.players.size - 1) {
                    teamId++
                    counter = 1
                    it.value.teamId = teamId.toString()
                } else {
                    it.value.teamId = teamId.toString()
                }
            }
        }

        //todo: добавить время начала игры
        //todo: добавить порядок игроков нормально + придумать как этот порядок будет использоваться
        game.players.withIndex().forEach { it.value.moveOrder = it.index }

        game.nextPlayerId = game.players.find { p -> p.moveOrder == 0 }?.id?.internalId ?: throw IllegalStateException()

        return gameSaver.save(game).toGameResponse()
    }
}
