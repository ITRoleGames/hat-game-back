package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import rubber.dutch.hat.domain.exception.PlayersLimitExceededException
import rubber.dutch.hat.domain.exception.UserNotJoinedException
import rubber.dutch.hat.domain.exception.WordsLimitExceededException
import java.util.*

@Entity
@Table(name = "game")
class Game(
  @Id
  @Column(name = "id", nullable = false)
  val id: GameId,

  @Column(nullable = false)
  val code: String,

  @Column(name = "creator_id")
  val creatorId: UserId,

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config")
  val config: GameConfig,

  @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
  @JoinColumn(name = "game_id")
  val players: MutableList<Player> = mutableListOf(),

  @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
  @JoinColumn(name = "game_id")
  val words: MutableList<WordInGame> = mutableListOf()
) {

  fun addPlayer(userId: UserId) {
      val isUserJoined = players.any { it.userId == userId }
      if (isUserJoined) {
          return
      }
      if (players.size >= MAX_PLAYERS_COUNT) {
          throw PlayersLimitExceededException()
      }
      players.add(
          Player(
              id = PlayerInternalId(),
              userId = userId,
              gameId = id,
              status = PlayerStatus.NEW,
              role = PlayerRole.PLAYER,
          )
      )
  }

  fun addWordsToPlayer(userId: UserId, words: List<String>) {
    val player = players.find { it.userId == userId }
      ?: throw UserNotJoinedException()

    if (player.words.size + words.size > config.wordsPerPlayer) {
      throw WordsLimitExceededException()
    }

    val wordsInGame = words.map {
      WordInGame(
        gameId = id,
        value = it,
        authorId = player.id.internalId!!,
        status = WordInGameStatus.AVAILABLE
      )
    }
    player.words.addAll(wordsInGame)

    if (player.words.size == config.wordsPerPlayer) {
      player.status = PlayerStatus.READY
    }
  }

    companion object {
        const val MAX_PLAYERS_COUNT = 8
    }

}
