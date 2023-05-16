package rubber.dutch.hat.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "word_in_game")
class WordInGame(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "game_id", nullable = false)
    val gameId: GameId,

    @Column(name = "author_id", nullable = false)
    val authorId: PlayerInternalId,

    @Column(name = "value", nullable = false)
    val value: String,

    @Column(name = "status", nullable = false)
    var status: WordInGameStatus,

    @Column(name = "explainer_id", nullable = true)
    var explainerId: PlayerInternalId = PlayerInternalId(),
) {

    fun isExplained() = status == WordInGameStatus.EXPLAINED
}

enum class WordInGameStatus {
    AVAILABLE,

    /**
     * Слово уже было в игре и было угадано
     */
    EXPLAINED,

    /**
     * Слово было в игре и было засвечено, объеснено не по правилам, выбыло из игры
     */
    FUCKUPED
}
