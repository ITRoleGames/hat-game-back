package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "round")
class Round(

    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Column(name = "explainer_id", nullable = false)
    val explainerId: PlayerInternalId,

    @Column(name = "game_id", nullable = false)
    val gameId: GameId,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "explanation_id")
    val explanation: MutableList<Explanation> = mutableListOf(),

    @Column(name = "start_time", nullable = false)
    val startTime: Instant,

    @Column(name = "status")
    val status: RoundStatus?
)

enum class RoundStatus {
    STARTED,
    FINISHED
}


