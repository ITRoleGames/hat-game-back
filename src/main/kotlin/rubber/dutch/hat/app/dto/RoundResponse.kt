package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.*
import java.time.Instant

data class RoundResponse(
    val id: RoundId,
    val explainerId: PlayerInternalId,
    val explanation: ExplanationResponse,
    val startTime: Instant
)

fun Round.toRoundResponse(): RoundResponse {
    return RoundResponse(
        id = id,
        explainerId = explainerId,
        explanation = getLastExplanation().toExplanationResponse(),
        startTime = startTime
    )
}
