package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.Explanation
import rubber.dutch.hat.domain.model.ExplanationId

class ExplanationDto(
    val id: ExplanationId,
    val wordId: Long,
    val wordValue: String
)

fun Explanation.toDto(): ExplanationDto {
    return ExplanationDto(
        id = id,
        wordId = word.id!!,
        wordValue = word.value,
    )
}
