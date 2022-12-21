package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.Game

interface GameSaver {

  fun save(game : Game) : Game
}