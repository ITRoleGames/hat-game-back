package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "explanation")
class Explanation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: ExplanationId,

    @Column(name = "round_id", nullable = false)
    val roundId: RoundId,

    @OneToOne
    @JoinColumn(name = "word_id", nullable = false)
    val wordInGameId: WordInGame,

    @Column(name = "start_time", nullable = false)
    val startTime: Instant = Instant.now(),

    @Column(name = "end_time")
    var endTime: Instant? = null,

    @Column(name = "result")
    var status: ExplanationResult? = null
)

enum class ExplanationResult {
    EXPLAINED,
    FAILED,
    TIMEOUTED
}
