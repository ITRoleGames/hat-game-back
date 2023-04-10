package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.Explanation

interface ExplanationRepository {
    fun save(explanation: Explanation): Explanation
}
