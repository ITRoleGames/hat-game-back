package rubber.dutch.hat.infra.db

import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.WordInGameStatus
import rubber.dutch.hat.domain.port.WordRepository

@Component
class WordRepositoryImpl(private val wordRepository: JpaWordRepository) : WordRepository {
    override fun availableWordsExistsInGame(game: Game): Boolean {
        return wordRepository.existsByStatusAndGameId(WordInGameStatus.AVAILABLE, game.id.gameId)
    }


}
