package rubber.dutch.hat.infra.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import rubber.dutch.hat.domain.model.Round

interface JpaRoundRepository : JpaRepository<Round, Long>, JpaSpecificationExecutor<Round> {
    fun save(round: Round): Round
}
