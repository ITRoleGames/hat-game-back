package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.port.GameRepository

@Component
class GameProvider(private val gameRepository: GameRepository) {

    fun findById(id: GameId): Game? {
        return gameRepository.findById(id.gameId)
    }

    fun findByCode(code: String): Game? {
        return gameRepository.findByCode(code)
    }

}
