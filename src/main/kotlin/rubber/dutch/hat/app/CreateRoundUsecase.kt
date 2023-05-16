package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import rubber.dutch.hat.app.dto.RoundResponse
import rubber.dutch.hat.app.dto.toRoundResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.domain.service.*
import rubber.dutch.hat.game.api.GameUpdatedEvent

@Component
class CreateRoundUsecase(
    private val gameProvider: GameProvider,
    private val roundSaver: RoundSaver,
    private val explanationSaver: ExplanationSaver,
    private val eventSender: EventSender
) {
    @Transactional
    fun execute(gameId: GameId, userId: UserId): RoundResponse {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        if (game.status != Game.GameStatus.STARTED) {
            throw GameStatusException()
        }

        val player = game.getPlayerByUserId(userId)
        val round = game.createRound(player.id)
        val savedRound = roundSaver.save(round)
        val newWord = game.getNewWord() ?: throw GameStatusException()
        val explanation = round.createExplanation(newWord)

        explanationSaver.save(explanation)
        eventSender.send(GameUpdatedEvent(game.id.gameId, userId.userId))

        return savedRound.toRoundResponse()
    }
}
