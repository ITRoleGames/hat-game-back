package rubber.dutch.hat.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

/**
 * Судя по последней схеме этот не нужен
 */
@Entity
@Table(name = "team")
class Team (
    @Id
    @Column(name = "id", nullable = false)
    val id: Long,

    @Column(name = "game_id", nullable = false)
    val gameId: GameId,

    @Column(name = "team_number")
    val teamNumber: Long,

    @Column(name = "next_user")
    val nextUserId: UserId,

    /**
     * массив ID пользователей
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "users")
    val users: MutableList<UserId> = mutableListOf(),

    /**
     * массив ID угаданных слов
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "guessed_words")
    val guessedWords: MutableList<Long> = mutableListOf()
)
