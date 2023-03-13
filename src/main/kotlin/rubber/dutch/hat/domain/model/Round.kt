package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "round")
class Round(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: RoundId,

    @Column(name = "explainer_id", nullable = false)
    val explainerId: PlayerInternalId,

    @Column(name = "game_id", nullable = false)
    val gameId: GameId,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "explanation_id")
    var explanation: MutableList<Explanation> = mutableListOf(),

    @Column(name = "start_time", nullable = false)
    val startTime: Instant = Instant.now(),

    @Column(name = "status", nullable = false)
    var status: RoundStatus = RoundStatus.STARTED
) {
    fun getLastExplanation(): Explanation {
        return explanation.maxByOrNull { it.startTime } ?: throw RuntimeException()
    }
}

enum class RoundStatus {
    STARTED,
    FINISHED
}


