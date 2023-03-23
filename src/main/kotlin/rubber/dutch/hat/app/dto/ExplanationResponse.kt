package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.Explanation
import rubber.dutch.hat.domain.model.ExplanationId

class ExplanationResponse(
    val id: ExplanationId,
    val wordId: Long,
    val wordValue: String
)

fun Explanation.toExplanationResponse(): ExplanationResponse {
    return ExplanationResponse(
        id = id,
        wordId = word.id!!,
        wordValue = word.value,
    )
}
