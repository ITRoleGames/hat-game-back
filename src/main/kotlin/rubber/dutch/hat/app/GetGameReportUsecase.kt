package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameReport
import rubber.dutch.hat.app.dto.TeamStat
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.PlayerInternalId
import rubber.dutch.hat.domain.service.GameProvider
import java.time.Duration
import java.time.Instant

@Component
class GetGameReportUsecase(private val gameProvider: GameProvider) {

    fun execute(gameId: GameId): GameReport {
        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()

        val totalWordsGuessed = game.words.count { it.isExplained() }

        val endTime = if (game.endTime != null) game.endTime else Instant.now()
        val totalTime = Duration.between(game.startTime, endTime).seconds

        val teamStats = mutableMapOf<Int, TeamStatAccumulator>()
        game.players.forEach {
            teamStats.getOrPut(it.teamId) { TeamStatAccumulator(it.teamId) }.players.add(it.id)
        }

        val playerToTeam = game.players.associate { it.id to it.teamId }
        game.words.filter { it.isExplained() }
            .forEach {
                val teamId = playerToTeam.getTeamId(it.explainerId)
                teamStats.getTeamStat(teamId).wordsGuessed++
            }

        game.rounds
            .forEach {
                val teamId = playerToTeam.getTeamId(it.explainerId)
                teamStats.getTeamStat(teamId).roundsCount++
            }

        return GameReport(
            wordsGuessed = totalWordsGuessed,
            totalTime = totalTime,
            teamStats = teamStats.values.map {
                TeamStat(
                    teamId = it.teamId,
                    players = it.players,
                    wordsGuessed = it.wordsGuessed,
                    roundsCount = it.roundsCount
                )
            }.sortedByDescending { it.wordsGuessed }
        )
    }

    private fun Map<PlayerInternalId, Int>.getTeamId(playerId: PlayerInternalId) =
        get(playerId) ?: error("teamId for player $playerId expected")

    private fun MutableMap<Int, TeamStatAccumulator>.getTeamStat(teamId: Int?) =
        get(teamId) ?: error("Statistics for team $teamId expected")
}

internal data class TeamStatAccumulator(
    val teamId: Int,
    val players: MutableList<PlayerInternalId> = mutableListOf(),
    var wordsGuessed: Int = 0,
    var roundsCount: Int = 0
)
