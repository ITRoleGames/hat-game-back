package rubber.dutch.hat.infra.db

import org.springframework.data.jpa.repository.JpaRepository
import rubber.dutch.hat.domain.model.Round
import java.util.*

interface JpaRoundRepository : JpaRepository<Round, UUID> {
    fun save(round: Round): Round
}
