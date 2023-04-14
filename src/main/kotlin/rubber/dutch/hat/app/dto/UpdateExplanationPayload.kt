package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.ExplanationId
import rubber.dutch.hat.domain.model.ExplanationResult

class UpdateExplanationPayload(
    val id: ExplanationId,
    val result: ExplanationResult
)
