package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.Round

interface RoundRepository {
    fun save(round: Round): Round
}
