package rubber.dutch.hat.app.dto

import rubber.dutch.hat.domain.model.*

data class PlayerDto(
    val id: PlayerInternalId,
    val userId: UserId,
    val status: PlayerStatus,
    var moveOrder: Int,
    var teamId: Int?,
    val role: PlayerRole
)

fun Player.toDto(): PlayerDto {
    return PlayerDto(
        id = id,
        userId = userId,
        status = status,
        moveOrder = moveOrder,
        teamId = teamId,
        role = role
    )
}
