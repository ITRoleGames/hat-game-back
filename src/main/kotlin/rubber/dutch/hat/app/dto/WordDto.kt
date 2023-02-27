package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.WordInGame
import rubber.dutch.hat.domain.model.WordInGameStatus

data class WordDto(
    val id: Long?,
    val word: String,
    val isGuessed: Boolean
)

fun WordInGame.toDto(): WordDto {
    return WordDto(
        id = id,
        word = value,
        isGuessed = status != WordInGameStatus.AVAILABLE
    )
}
