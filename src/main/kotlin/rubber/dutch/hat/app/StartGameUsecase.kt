package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.toGameResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.port.GameSaver
import rubber.dutch.hat.domain.service.GameProvider

const val teamSize = 2

/**
 * Примеры распределения игроков по командам
 *
 * Для 5 игроков, команда из 2х игроков:
 * playerIndex=0 teamId=1
 * playerIndex=1 teamId=1
 * playerIndex=2 teamId=2
 * playerIndex=3 teamId=2
 * playerIndex=4 teamId=2
 *
 * Для 4 игроков, команда из 2х игроков:
 * playerIndex=0 teamId=1
 * playerIndex=1 teamId=1
 * playerIndex=2 teamId=2
 * playerIndex=3 teamId=2
 *
 * Метод +- сносно работает для команды из 3х игроков и более, но есть нюансы:)
 */
@Component
class StartGameUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver,
) {

    fun execute(gameId: GameId): GameResponse {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()

        var teamId = 0
        //TODO: можно убрать дублирование кода
        if (game.players.size % teamSize != 0) {
            //Количество игроков НЕ делится на количество игроков в команде без остатка
            game.players.shuffled().withIndex().forEach {
                if (it.index % teamSize == 0
                    && it.index != (game.players.size - 1)) { //В таком случае последний игрок попадает в ту же команду что и предыдущие 2 игрока
                    teamId++
                }
                it.value.teamId = teamId.toString()
            }
        } else {
            //Количество игроков делится на количество игроков в команде без остатка
            game.players.shuffled().withIndex().forEach {
                if (it.index % teamSize == 0) {
                    teamId++
                }
                it.value.teamId = teamId.toString()
            }
        }

        //todo: добавить время начала игры
        //todo: добавить порядок игроков нормально + придумать как этот порядок будет использоваться
        game.players.withIndex().forEach { it.value.moveOrder = it.index }

        game.nextPlayerId = game.players.find { p -> p.moveOrder == 0 }?.id?.internalId ?: throw IllegalStateException()

        return gameSaver.save(game).toGameResponse()
    }
}
