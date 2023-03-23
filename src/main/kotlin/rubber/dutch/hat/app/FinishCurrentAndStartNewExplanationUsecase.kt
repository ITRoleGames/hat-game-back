package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.ExplanationResponse
import rubber.dutch.hat.domain.exception.ExplanationResultException
import rubber.dutch.hat.app.dto.UpdateExplanationRequest
import rubber.dutch.hat.app.dto.toExplanationResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.RoundNotFoundException
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.service.ExplanationSaver
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameSaver

@Component
class FinishCurrentAndStartNewExplanationUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver,
    private val explanationSaver: ExplanationSaver,
) {
    fun execute(
        gameId: GameId,
        roundId: RoundId,
        request: UpdateExplanationRequest,
        userId: UserId
    ): ExplanationResponse {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        val round = game.getLastRound()

        if (round!!.id != roundId) {
            throw RoundNotFoundException()
        }
        val explanation = round.getExplanationById(request.id)
        if (explanation.result != null) {
            throw ExplanationResultException()
        }

        explanation.result = ExplanationResult.valueOf(request.result)
        explanation.endTime = request.endTime

        val word = explanation.word

        when (request.result) {
            ExplanationResult.EXPLAINED.toString() -> word.status = WordInGameStatus.EXPLAINED
            ExplanationResult.FAILED.toString() -> word.status = WordInGameStatus.FUCKUPED
        }
        explanationSaver.update(explanation)

        val newExplanation = round.createExplanation(game.getNewWord())

        explanationSaver.save(newExplanation)
        gameSaver.saveAndNotify(game, userId)

        return newExplanation.toExplanationResponse()
    }
}
