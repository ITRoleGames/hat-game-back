package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.GameId

data class AddWordsRequestPayload(
    val gameId: GameId,
    val words: List<String>
)
