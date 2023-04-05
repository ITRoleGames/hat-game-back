package rubber.dutch.hat.app

import org.springframework.stereotype.Service
import rubber.dutch.hat.app.dto.GameReport
import rubber.dutch.hat.app.dto.TeamStat
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.PlayerInternalId
import rubber.dutch.hat.domain.service.GameProvider
import java.time.Duration
import java.time.Instant

@Service
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
                val teamId = playerToTeam[it.explainerId]!!
                teamStats[teamId]!!.wordsGuessed++
            }

        game.rounds
            .forEach {
                val teamId = playerToTeam[it.explainerId]!!
                teamStats[teamId]!!.roundsCount++
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
}

internal data class TeamStatAccumulator(
    val teamId: Int,
    val players: MutableList<PlayerInternalId> = mutableListOf(),
    var wordsGuessed: Int = 0,
    var roundsCount: Int = 0
)
