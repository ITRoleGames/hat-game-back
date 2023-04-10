package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.ExplanationResponse
import rubber.dutch.hat.app.dto.UpdateExplanationPayload
import rubber.dutch.hat.app.dto.toExplanationResponse
import rubber.dutch.hat.domain.exception.ExplanationResultException
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.ResultException
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
    fun execute(
        gameId: GameId,
        roundId: RoundId,
        request: UpdateExplanationPayload,
        userId: UserId
    ): ExplanationResponse {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        val round = game.getCurrentRound()

        if (round.id != roundId) {
            throw RoundNotFoundException()
        }
        val explanation = round.getExplanationById(request.id)
        if (explanation.result != null) {
            throw ExplanationResultException()
        }
        explanation.result = request.result
        explanation.endTime = Instant.now()
        val word = explanation.word

        when (request.result) {
            ExplanationResult.EXPLAINED -> word.status = WordInGameStatus.EXPLAINED
            ExplanationResult.FAILED -> word.status = WordInGameStatus.FUCKUPED
            else -> throw ResultException()
        }
        explanationSaver.save(explanation)

        val newExplanation = round.createExplanation(game.getNewWord())

        explanationSaver.save(newExplanation)
        eventSender.send(GameUpdatedEvent(game.id.gameId, userId.userId))
        return newExplanation.toExplanationResponse()
    }
}
