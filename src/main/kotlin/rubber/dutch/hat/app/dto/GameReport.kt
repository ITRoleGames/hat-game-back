package rubber.dutch.hat.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import rubber.dutch.hat.domain.model.PlayerInternalId

data class GameReport(
    @field:Schema(description = "Общее количество отгаданных слов в игре.")
    val wordsGuessed: Int,
    @field:Schema(description = "Общая продолжительность игры в секундах.")
    val totalTime: Long,
    @field:Schema(description = "Данные по игре каждой команды. Список упорядочен от первого места к последнему.")
    val teamStats: List<TeamStat>
)

data class TeamStat(
    @field:Schema(description = "Внутренний ID команды.")
    val teamId: Int,
    @field:Schema(description = "Список ID игроков.")
    val players: List<PlayerInternalId>,
    @field:Schema(description = "Количество отгаданных командой слов.")
    val wordsGuessed: Int,
    @field:Schema(description = "Количество сыграных командой раундов.")
    val roundsCount: Int
)
