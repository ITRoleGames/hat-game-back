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
    val id: Long,

    @Column(name = "round_id")
    val roundId: RoundId,

    @Column(name = "word_id")
    val wordInGameId: Long,

    @Column(name = "start_time")
    val startTime: Instant = Instant.now(),

    @Column(name = "end_time")
    var endTime: Instant,

    @Column(name = "result")
    var status: ExplanationResult?
)

enum class ExplanationResult {
    EXPLAINED,
    FAILED,
    TIMEOUTED
}
