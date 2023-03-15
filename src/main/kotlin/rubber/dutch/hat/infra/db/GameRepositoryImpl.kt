package rubber.dutch.hat.infra.db

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.port.GameRepository
import java.util.*

@Component
class GameRepositoryImpl(
    private val jpaGameRepository: JpaGameRepository
) : GameRepository {

    override fun save(game: Game): Game {
        return jpaGameRepository.save(game)
    }

    override fun findById(id: UUID): Game? {
        return jpaGameRepository.findById(id).orElse(null)
    }

    override fun findByCode(code: String): Game? {
        return jpaGameRepository.findByCode(code)
    }

}
