package rubber.dutch.hat.infra.db

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Player
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.port.PlayerRepository

@Component
class PlayerRepositoryImpl(private val playerRepository: JpaPlayerRepository) : PlayerRepository {
    override fun findByUserId(userId: UserId): Player? {
        return playerRepository.findByUserId(userId.userId)
    }
}
