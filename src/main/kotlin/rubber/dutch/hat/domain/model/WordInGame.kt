package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import java.util.*

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
  val authorId: Long,

  @Column(name = "value", nullable = false)
  val value: String,

  @Column(name = "status", nullable = false)
  val status: WordInGameStatus,

  @Column(name = "explainer_id", nullable = true)
  val explainerId: Long? = null,
)

enum class WordInGameStatus {
  AVAILABLE,
  EXPLAINED,
  FUCKUPED
}