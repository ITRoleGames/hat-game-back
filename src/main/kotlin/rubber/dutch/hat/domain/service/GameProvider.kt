package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.port.GameFinder
import java.util.*

@Component
class GameProvider(private val gameFinder: GameFinder) {

  fun findById(code : UUID): Game? {
    return gameFinder.findById(code)
  }

  fun findByCode(code : String): Game? {
    return gameFinder.findByCode(code)
  }

}