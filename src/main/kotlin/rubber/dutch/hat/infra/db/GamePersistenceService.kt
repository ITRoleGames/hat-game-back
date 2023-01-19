package rubber.dutch.hat.infra.db

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.port.GameFinder
import rubber.dutch.hat.domain.port.GameSaver
import java.util.*

@Component
class GamePersistenceService(private val gameRepository: GameRepository) : GameSaver, GameFinder {

    override fun save(game: Game): Game {
        return gameRepository.save(game)
    }

    override fun findByCode(code: String): Game? {
        return gameRepository.findByCode(code)
    }

    override fun findById(id: UUID): Game? {

        return gameRepository.findByGameId(id)
    }
}