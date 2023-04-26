package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.Explanation
import rubber.dutch.hat.domain.model.ExplanationId

class ExplanationResponse(
    @field:Schema(description = "Id объясянения")
    val id: ExplanationId,

    @field:Schema(description = "Id слова")
    val wordId: Long,

    @field:Schema(description = "Отгадываемое слово")
    val wordValue: String
)

fun Explanation.toExplanationResponse(): ExplanationResponse {
    if (word.id == null) {
        throw IllegalStateException("Explanation is not a saved entity")
    }

    return ExplanationResponse(
        id = id,
        wordId = word.id,
        wordValue = word.value,
    )
}
