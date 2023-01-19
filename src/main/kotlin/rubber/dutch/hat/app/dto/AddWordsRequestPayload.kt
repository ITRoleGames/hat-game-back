package rubber.dutch.hat.app.dto

import java.util.UUID

data class AddWordsRequestPayload(
  val gameId: UUID,
  val words: List<String>
)