package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.*
import java.time.Instant

data class RoundResponse(
    @field:Schema(description = "Id игры")
    val id: RoundId,

    @field:Schema(description = "Id объясняющего игрока ")
    val explainerId: PlayerInternalId,

    @field:Schema(description = "Объяснение")
    val explanation: ExplanationResponse,

    @field:Schema(description = "Время начала раунда")
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
