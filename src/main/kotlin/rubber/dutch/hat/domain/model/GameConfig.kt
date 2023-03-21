package rubber.dutch.hat.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GameConfig(
    @JsonProperty("wordsPerPlayer")
    val wordsPerPlayer: Int,
    @JsonProperty("moveTime")
    val moveTime: Int
)
