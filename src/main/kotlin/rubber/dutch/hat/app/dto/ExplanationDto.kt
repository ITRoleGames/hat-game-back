
package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.Explanation
import rubber.dutch.hat.domain.model.ExplanationId

class ExplanationDto(
    val id: ExplanationId,
    val wordInGameId: Long?,
    val wordInGameValue: String
)

fun Explanation.toDto(): ExplanationDto{
    return ExplanationDto(
        id = id,
        wordInGameId = wordInGameId.id,
        wordInGameValue = wordInGameId.value,
    )

}

