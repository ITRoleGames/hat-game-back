package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.Game
import java.util.*

interface GameFinder {

  fun findByCode(code: String) : Game?

  fun findById(id: UUID) : Game?
}