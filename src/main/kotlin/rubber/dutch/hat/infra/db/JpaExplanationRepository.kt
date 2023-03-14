package rubber.dutch.hat.infra.db

import org.springframework.data.jpa.repository.JpaRepository
import rubber.dutch.hat.domain.model.Explanation

interface JpaExplanationRepository : JpaRepository<Explanation, Long>
