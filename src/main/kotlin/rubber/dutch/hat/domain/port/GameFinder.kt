package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.Game

interface GameFinder {

  fun findByCode(code: String) : Game?

}