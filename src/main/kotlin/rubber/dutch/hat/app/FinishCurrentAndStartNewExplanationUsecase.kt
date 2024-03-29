package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import rubber.dutch.hat.app.dto.ExplanationResponse
import rubber.dutch.hat.app.dto.UpdateExplanationPayload
import rubber.dutch.hat.app.dto.toExplanationResponse
import rubber.dutch.hat.domain.exception.ExplanationResultException
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.RoundNotFoundException
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.domain.service.ExplanationSaver
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.game.api.GameUpdatedEvent
import java.time.Instant

@Component
class FinishCurrentAndStartNewExplanationUsecase(
    private val gameProvider: GameProvider,
    private val explanationSaver: ExplanationSaver,
    private val eventSender: EventSender
) {
    @Transactional
    fun execute(
        gameId: GameId,
        roundId: RoundId,
        payload: UpdateExplanationPayload,
        userId: UserId
    ): ExplanationResponse? {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        val round = game.getCurrentRound()

        if (round.id != roundId) {
            throw RoundNotFoundException()
        }
        val explanation = round.getExplanationById(payload.id)
        if (explanation.result != null) {
            throw ExplanationResultException("Current explanation is completed")
        }
        explanation.result = payload.result
        explanation.endTime = Instant.now()
        val word = explanation.word

        when (payload.result) {
            ExplanationResult.EXPLAINED -> word.status = WordInGameStatus.EXPLAINED
            ExplanationResult.FAILED -> word.status = WordInGameStatus.FUCKUPED
            else -> throw ExplanationResultException("Incorrect result")
        }
        game.getPlayerByUserId(userId).also { word.explainerId = it.id }
        explanationSaver.save(explanation)

        return game.getNewWord()?.let {
            val newExplanation = round.createExplanation(it)
            explanationSaver.save(newExplanation)
            eventSender.send(GameUpdatedEvent(game.id.gameId, userId.userId))
            newExplanation.toExplanationResponse()
        }
    }
}
