package rubber.dutch.hat.infra.db

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Round
import rubber.dutch.hat.domain.port.RoundRepository

@Component
class RoundRepositoryImpl(private val roundRepository: JpaRoundRepository) : RoundRepository {
    override fun save(round: Round): Round {
        return roundRepository.save(round)
    }
}
