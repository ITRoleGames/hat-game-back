package rubber.dutch.hat.infra.db

import org.springframework.data.jpa.repository.JpaRepository
import rubber.dutch.hat.domain.model.Player
import rubber.dutch.hat.domain.model.UserId
import java.util.*

interface JpaPlayerRepository : JpaRepository<Player, Long> {

    fun findByUserId(userId: UUID) : Player?
}
