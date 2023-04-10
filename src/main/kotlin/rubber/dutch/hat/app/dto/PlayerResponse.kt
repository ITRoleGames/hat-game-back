package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.*

data class PlayerResponse(
    @field:Schema(description = "ID игрока")
    val id: PlayerInternalId,

    @field:Schema(description = "ID юзера")
    val userId: UserId,

    @field:Schema(description = "Статус игрока")
    val status: PlayerStatus,

    @field:Schema(description = "Очередность хода")
    var moveOrder: Int,

    @field:Schema(description = "ID команды")
    var teamId: Int?,

    @field:Schema(description = "Роль иирока")
    val role: PlayerRole
)

fun Player.toResponse(): PlayerResponse {
    return PlayerResponse(
        id = id,
        userId = userId,
        status = status,
        moveOrder = moveOrder,
        teamId = teamId,
        role = role
    )
}
