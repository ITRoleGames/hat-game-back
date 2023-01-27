package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.Game
import java.util.*

interface GameFinder {

  fun findById(id: UUID) : Game?

  fun findByCode(code: String) : Game?
}
