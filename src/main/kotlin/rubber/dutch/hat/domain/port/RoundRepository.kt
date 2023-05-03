package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.dto.FindRoundsCriteria
import rubber.dutch.hat.domain.model.Round

interface RoundRepository {
    fun save(round: Round): Round
    fun find(criteria: FindRoundsCriteria): List<Round>
}
