package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import rubber.dutch.hat.domain.exception.UserNotJoinedException
import rubber.dutch.hat.domain.exception.WordsLimitExceededException
import java.util.*

@Entity
@Table(name = "game")
class Game(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  val id: Long? = null,

  @Column(name = "game_id", nullable = false)
  val gameId: UUID,

  @Column(nullable = false)
  val code: String,

  @Column(name = "creator_id")
  val creatorId: UUID,

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config")
  val config: GameConfig,

  @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
  @JoinColumn(name = "game_id")
  val players: MutableList<Player> = mutableListOf(),

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id")
  val words: MutableList<WordInGame> = mutableListOf()
) {

  fun userIsJoined(userId: UUID): Boolean {
    return players.any { it.userId == userId }
  }

  fun addPlayer(userId: UUID) {
    players.add(
      Player(
        userId = userId,
        gameId = id!!,
        status = PlayerStatus.NEW,
        role = PlayerRole.PLAYER,
      )
    )
  }

  fun addWordsToPlayer(userId: UUID, words: List<String>) {
    val player = players.find { it.userId == userId }
      ?: throw UserNotJoinedException()

    if (player.words.size + words.size > config.wordsPerPlayer) {
      throw WordsLimitExceededException()
    }

    val wordsInGame = words.map {
      WordInGame(
        gameId = id!!,
        value = it,
        authorId = player.id!!,
        status = WordInGameStatus.AVAILABLE
      )
    }
    player.words.addAll(wordsInGame)

    if (player.words.size == config.wordsPerPlayer) {
      player.status = PlayerStatus.READY
    }
  }
}
