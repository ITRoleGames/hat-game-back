package rubber.dutch.hat.infra.db

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Explanation
import rubber.dutch.hat.domain.port.ExplanationRepository

@Component
class ExplanationRepositoryImpl (private val explanationRepository: JpaExplanationRepository) : ExplanationRepository {
    override fun save(explanation: Explanation): Explanation {
        return explanationRepository.save(explanation)
    }
}
