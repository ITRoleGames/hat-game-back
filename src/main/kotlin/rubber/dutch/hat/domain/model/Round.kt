package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import rubber.dutch.hat.domain.exception.ExplanationNotFoundException
import rubber.dutch.hat.domain.exception.ExplanationResultException
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
    @JoinColumn(name = "round_id")
    var explanations: MutableList<Explanation> = mutableListOf(),

    @Column(name = "start_time", nullable = false)
    val startTime: Instant = Instant.now(),

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: RoundStatus = RoundStatus.STARTED
) {
    fun getLastExplanation(): Explanation {
        return explanations.maxByOrNull { it.startTime } ?: throw ExplanationNotFoundException()
    }

    fun createExplanation(word: WordInGame): Explanation {
        val explanation = Explanation(
            id = ExplanationId(),
            roundId = id,
            word = word
        )

        explanations.add(explanation)
        return explanation
    }

    fun finishCurrentExplanation(explanationId: ExplanationId, result: ExplanationResult) {
        val explanation = getExplanationById(explanationId)
        explanation.result = result
        explanation.endTime = Instant.now()
        val word = explanation.word

        when (result) {
            ExplanationResult.EXPLAINED -> word.status = WordInGameStatus.EXPLAINED
            ExplanationResult.FAILED -> word.status = WordInGameStatus.FUCKUPED
            else -> throw ExplanationResultException("что-то пошло не так")
        }
    }

    fun getExplanationById(explanationId: ExplanationId): Explanation {
        return explanations.firstOrNull { it.id == explanationId } ?: throw ExplanationNotFoundException()
    }

    enum class RoundStatus {
        STARTED,
        FINISHED
    }
}
