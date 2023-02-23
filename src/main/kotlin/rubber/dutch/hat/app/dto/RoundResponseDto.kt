package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.PlayerInternalId
import rubber.dutch.hat.domain.model.Round
import rubber.dutch.hat.domain.model.RoundId
import java.time.Instant
import java.util.*

data class RoundResponseDto(
    val roundId: RoundId,
    val gameId: GameId,
    val playerId: PlayerInternalId,
    val createdTime: Instant
)

fun Round.toDto(): RoundResponseDto {
    return RoundResponseDto(
        roundId = id,
        gameId = gameId,
        playerId = explainerId,
        createdTime = startTime
    )
}
