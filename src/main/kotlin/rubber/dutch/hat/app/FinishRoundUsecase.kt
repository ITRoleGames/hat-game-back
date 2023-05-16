package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.exception.RoundNotFoundException
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.domain.port.WordRepository
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameSaver
import rubber.dutch.hat.domain.service.RoundSaver
import rubber.dutch.hat.game.api.GameUpdatedEvent
import java.time.Instant

@Component
class FinishRoundUsecase(
    private val gameProvider: GameProvider,
    private val gameSaver: GameSaver,
    private val roundSaver: RoundSaver,
    private val eventSender: EventSender,
    private val wordRepository: WordRepository
) {
    fun execute(gameId: GameId, userId: UserId, roundId: RoundId) {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        if (game.status != Game.GameStatus.STARTED) {
            throw GameStatusException()
        }

        val round = game.getCurrentRound()
        if (round.id != roundId) {
            throw RoundNotFoundException()
        }

        val explanation = round.getLastExplanation()
        explanation.result = ExplanationResult.TIMEOUTED
        explanation.endTime = Instant.now()
        round.status = Round.RoundStatus.FINISHED

        roundSaver.save(round)

        if (!wordRepository.availableWordsExistsInGame(game)) {
            game.finish()
            gameSaver.saveAndNotify(game, userId)
        } else {
            eventSender.send(GameUpdatedEvent(game.id.gameId, userId.userId))
        }
    }
}
