package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Round
import rubber.dutch.hat.domain.port.RoundRepository

@Component
class RoundSaver(private val roundRepository: RoundRepository) {
    fun save(round: Round): Round {
        return roundRepository.save(round)
    }
}
