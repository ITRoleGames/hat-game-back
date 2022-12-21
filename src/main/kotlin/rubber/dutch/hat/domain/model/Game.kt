package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "game")
data class Game(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(nullable = false)
  val gameId: String,

  @Column(nullable = false)
  val code: String,

  @Column(name = "creator_id")
  val creatorId: String,

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config")
  val config: GameConfig,

  @ElementCollection
  @CollectionTable(name = "game_to_user", joinColumns = [JoinColumn(name = "game_id")])
  @Column(name = "user_id")
  val users: MutableSet<String> = mutableSetOf()

) {

  fun userIsJoined(userId: String): Boolean {
    return users.contains(userId)
  }
}
