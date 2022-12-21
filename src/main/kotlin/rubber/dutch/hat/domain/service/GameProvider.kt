package rubber.dutch.hat.domain.service

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.port.GameFinder

@Component
class GameProvider(private val gameFinder: GameFinder) {

  fun findByCode(code : String): Game? {
    return gameFinder.findByCode(code)
  }

}