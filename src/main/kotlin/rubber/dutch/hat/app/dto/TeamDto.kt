package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.*

data class TeamDto (
    val id: Long,
    val teamNumber: Long,
    val userIds: List<UserId>
)
