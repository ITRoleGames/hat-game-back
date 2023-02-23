package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.Player
import rubber.dutch.hat.domain.model.UserId

interface PlayerRepository {

    fun findByUserId(userId: UserId): Player?
}
