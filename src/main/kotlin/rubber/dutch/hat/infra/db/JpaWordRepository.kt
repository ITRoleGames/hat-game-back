package rubber.dutch.hat.infra.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import rubber.dutch.hat.domain.model.WordInGame
import rubber.dutch.hat.domain.model.WordInGameStatus
import java.util.UUID

interface JpaWordRepository : JpaRepository<WordInGame, Long>, JpaSpecificationExecutor<WordInGame> {
    fun existsByStatusAndGameId(status: WordInGameStatus, gameId: UUID): Boolean
}
