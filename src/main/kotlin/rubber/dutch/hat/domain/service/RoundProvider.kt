package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.dto.FindRoundsCriteria
import rubber.dutch.hat.domain.model.Round
import rubber.dutch.hat.domain.port.RoundRepository

@Component
class RoundProvider(private val roundRepository: RoundRepository) {

    fun find(criteria: FindRoundsCriteria): List<Round> {
        return roundRepository.find(criteria)
    }
}
