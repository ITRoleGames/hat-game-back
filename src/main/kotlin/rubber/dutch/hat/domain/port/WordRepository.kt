package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.Game

interface WordRepository {

    fun availableWordsExistsInGame(game: Game): Boolean
}
