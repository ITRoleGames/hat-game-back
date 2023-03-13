package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.*
import java.time.Instant

data class RoundDto(
    val roundId: RoundId,
    val explainerId: PlayerInternalId,
    val explanationId: ExplanationDto


)

fun Round.toDto(): RoundDto {
    return RoundDto(
        roundId = id,
        explainerId = explainerId,
        explanationId = getLastExplanation().toDto()
    )
}
