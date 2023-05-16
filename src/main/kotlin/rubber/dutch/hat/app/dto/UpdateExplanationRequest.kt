package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.ExplanationId
import java.time.Instant

class UpdateExplanationRequest(
    val id: ExplanationId,
    val result: String,
    val endTime: Instant
)
