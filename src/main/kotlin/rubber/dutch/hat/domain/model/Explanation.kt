package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "explanation")
class Explanation(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Column(name = "round_id")
    val roundId: RoundId,

    @Column(name = "word_id")
    val wordInGameId: UUID,

    @Column(name = "start_time")
    val startTime: Instant,

    @Column(name = "end_time")
    val endTime: Instant,

    @Column(name = "result")
    val status: ExplanationResult?
)

enum class ExplanationResult {
    EXPLAINED,
    FAILED,
    TIMEOUTED
}
