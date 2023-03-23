package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.RoundResponse
import rubber.dutch.hat.app.dto.toRoundResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.exception.MoveOrderException
import rubber.dutch.hat.domain.exception.RoundStatusException
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.service.*

@Component
class CreateRoundUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver,
    private val roundSaver: RoundSaver,
    private val explanationSaver: ExplanationSaver
) {
    fun execute(gameId: GameId, userId: UserId): RoundResponse {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        if (game.status != Game.GameStatus.STARTED) {
            throw GameStatusException()
        }

        val player = game.getPlayerByUserId(userId)
        val lastRound = game.getLastRound()

        if (!isCorrectMoveOrder(game, lastRound, player)) {
            throw MoveOrderException()
        }

        val round = game.createRound(player.id, game.id)
        val savedRound = roundSaver.save(round)
        val explanation = round.createExplanation(game.getNewWord())

        explanationSaver.save(explanation)
        gameSaver.saveAndNotify(game, userId)

        return savedRound.toRoundResponse()
    }

    private fun isCorrectMoveOrder(game: Game, round: Round?, currentPlayer: Player): Boolean {
        return if (round != null) {
            if (round.status != Round.RoundStatus.FINISHED) {
                throw RoundStatusException()
            }
            val lastPlayer = game.getPlayerByPlayerId(round.explainerId)
            if (lastPlayer.moveOrder != game.players.size - 1) {
                lastPlayer.moveOrder + 1 == currentPlayer.moveOrder
            } else {
                currentPlayer.moveOrder == 0
            }
        } else {
            currentPlayer.moveOrder == 0
        }
    }
}
