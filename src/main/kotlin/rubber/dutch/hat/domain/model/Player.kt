package rubber.dutch.hat.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "player")
class Player(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  val id: PlayerInternalId,

  @Column(name = "user_id")
  val userId: UserId,

  @Column(name = "game_id")
  val gameId: GameId,

  @Column(name = "status", nullable = false)
  var status: PlayerStatus,

  @Column(name = "move_order", nullable = false)
  var moveOrder: Int = 0,

  @Column(name = "team_id")
  var teamId: String? = null,

  @Column(name = "role", nullable = false)
  val role: PlayerRole,

  @OneToMany(cascade = [CascadeType.ALL])
  @JoinColumn(name = "author_id")
  val words: MutableList<WordInGame> = mutableListOf()
)

enum class PlayerStatus {
  NEW,
  READY
}

enum class PlayerRole {
  PLAYER,
  ORGANIZER
}
