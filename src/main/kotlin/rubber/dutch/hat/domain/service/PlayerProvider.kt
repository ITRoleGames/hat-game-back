package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Player
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.port.PlayerRepository

@Component
class PlayerProvider(private val playerRepository: PlayerRepository) {

    fun finByUserId(userId: UserId): Player? {
        return playerRepository.findByUserId(userId)
    }
}
