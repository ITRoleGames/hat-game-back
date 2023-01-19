package rubber.dutch.hat.infra.db

import org.springframework.data.jpa.repository.JpaRepository
import rubber.dutch.hat.domain.model.Game
import java.util.UUID

interface GameRepository : JpaRepository<Game, Long> {

  fun findByCode(code: String): Game?

  fun findByGameId(id: UUID): Game?
}