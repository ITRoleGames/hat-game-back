package rubber.dutch.hat.infra.db

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.dto.FindRoundsCriteria
import rubber.dutch.hat.domain.model.Round
import rubber.dutch.hat.domain.model.Round_
import rubber.dutch.hat.domain.port.RoundRepository
import java.util.*

@Component
class RoundRepositoryImpl(private val roundRepository: JpaRoundRepository) : RoundRepository {
    override fun save(round: Round): Round {
        return roundRepository.save(round)
    }

    override fun find(criteria: FindRoundsCriteria): List<Round> {
        val page = PageRequest.of(0, criteria.limit, Sort.by(criteria.sort, Round_.startTime.name))
        return roundRepository.findAll(Specification.where(hasGameId(criteria.gameId.gameId)), page).toList()
    }

    private fun hasGameId(gameId: UUID): Specification<Round>? {
        return Specification<Round> { round, _, criteriaBuilder ->
            criteriaBuilder.equal(round.get(Round_.gameId), gameId)
        }
    }
}
