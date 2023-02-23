package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.port.RoundRepository
import java.time.Instant
import java.util.*

@Component
class RoundCreator(private val roundSaver: RoundRepository) {
//    fun createRound(gameId: GameId, playerId: PlayerInternalId): Round {
//        val round = Round(
//            id = RoundId(),
//            explainerId = playerId,
//            gameId = gameId,
//            startTime = Instant.now(),
//            status = RoundStatus.STARTED
//        )
//        return roundSaver.save(round)
//    }
}
