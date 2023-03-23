package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Explanation
import rubber.dutch.hat.domain.port.ExplanationRepository

@Component
class ExplanationSaver(private val explanationRepository: ExplanationRepository) {
    fun save(explanation: Explanation): Explanation {
        return explanationRepository.save(explanation)
    }

    fun update(explanation: Explanation): Explanation {
        return explanationRepository.update(explanation)
    }
}
