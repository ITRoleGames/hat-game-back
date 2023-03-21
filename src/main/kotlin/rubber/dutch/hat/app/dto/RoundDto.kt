package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.*

data class RoundDto(
    val id: RoundId,
    val explainerId: PlayerInternalId,
    val newExplanation: ExplanationDto
)

fun Round.toDto(): RoundDto {
    return RoundDto(
        id = id,
        explainerId = explainerId,
        newExplanation = getLastExplanation().toDto()
    )
}
