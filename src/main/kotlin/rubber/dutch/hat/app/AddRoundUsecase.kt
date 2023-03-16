package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.RoundDto
import rubber.dutch.hat.app.dto.toDto
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.service.*

@Component
class AddRoundUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver,
    private val roundSaver: RoundSaver,
    private val explanationSaver: ExplanationSaver
) {
    fun execute(gameId: GameId, userId: UserId): RoundDto {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        if (game.status != Game.GameStatus.STARTED) {
            throw GameStatusException()
        }

        val player = game.getPlayerByUserId(userId)
//        todo: сделать проверку на очередь игрока
        val round = game.addNewRound(player.id, game.id)
        val savedRound = roundSaver.save(round)
        val explanation = round.addNewExplanation(game.getNewWord())

        explanationSaver.save(explanation)
        gameSaver.saveAndNotify(game, userId)

        return savedRound.toDto()
    }
}
