package rubber.dutch.hat.infra.api.dto

import jakarta.validation.constraints.Min
import org.springframework.data.domain.Sort
import rubber.dutch.hat.domain.dto.FindRoundsCriteria
import rubber.dutch.hat.domain.model.GameId

data class GetRoundsCriteria(@Min(1) val limit: Int, val sort: Sort.Direction) {

    fun toFindCriteria(gameId: GameId): FindRoundsCriteria = FindRoundsCriteria(gameId, limit, sort)
}
