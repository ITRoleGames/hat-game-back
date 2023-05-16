package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.RoundResponse
import rubber.dutch.hat.app.dto.toRoundResponse
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.GameStatusException
import rubber.dutch.hat.domain.model.Game
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.RoundProvider
import rubber.dutch.hat.infra.api.dto.GetRoundsCriteria

@Component
class GetRoundUsecase(
    private val gameProvider: GameProvider,
    private val roundProvider: RoundProvider
) {
    fun execute(gameId: GameId, userId: UserId, criteria: GetRoundsCriteria): List<RoundResponse> {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()
        if (game.status == Game.GameStatus.NEW) throw GameStatusException()

        return roundProvider.find(criteria.toFindCriteria(gameId)).map { it.toRoundResponse() }
    }
}
