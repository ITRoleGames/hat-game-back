package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "game")
class Game(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  val id: Long? = null,

  @Column(name="game_id", nullable = false)
  val gameId: UUID,

  @Column(nullable = false)
  val code: String,

  @Column(name = "creator_id")
  val creatorId: UUID,

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config")
  val config: GameConfig,

  @ElementCollection
  @CollectionTable(name = "game_to_user", joinColumns = [JoinColumn(name = "game_id")])
  @Column(name = "user_id")
  val users: MutableSet<UUID> = mutableSetOf()

) {

  fun userIsJoined(userId: UUID): Boolean {
    return users.contains(userId)
  }
}
