package rubber.dutch.hat.domain.dto

import org.springframework.data.domain.Sort
import rubber.dutch.hat.domain.model.GameId

data class FindRoundsCriteria(val gameId: GameId, val limit: Int, val sort: Sort.Direction)
