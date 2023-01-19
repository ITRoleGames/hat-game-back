package rubber.dutch.hat.infra.db

import org.springframework.data.jpa.repository.JpaRepository
import rubber.dutch.hat.domain.model.Game
import java.util.*

interface GameRepository : JpaRepository<Game, Long> {

  fun findByGameId(gameId: UUID): Game?

  fun findByCode(code: String): Game?
}