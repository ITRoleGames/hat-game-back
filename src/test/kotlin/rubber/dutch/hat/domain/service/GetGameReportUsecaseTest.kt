package rubber.dutch.hat.domain.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import rubber.dutch.hat.app.GetGameReportUsecase
import rubber.dutch.hat.domain.model.*
import java.time.Instant
import java.util.*

@ExtendWith(MockitoExtension::class)
class GetGameReportUsecaseTest {

    @Mock
    lateinit var gameProvider: GameProvider

    @InjectMocks
    lateinit var usecase: GetGameReportUsecase

    @Test
    fun execute_success() {
        val wordsPerPlayer = 10
        val playersCount = MAX_PLAYERS_COUNT
        val roundsCount = playersCount * 5
        val totalTime = 1024L
        val wordsCount = wordsPerPlayer * playersCount

        val now = Instant.now()
        val game = Game(
            id = GameId(UUID.randomUUID()),
            code = "code",
            creatorId = UserId(UUID.randomUUID()),
            config = GameConfig(wordsPerPlayer, 30),
            status = Game.GameStatus.FINISHED,
            startTime = now,
            endTime = now.plusSeconds(totalTime)
        )

        val teamToPlayers = mutableMapOf<Int, MutableSet<PlayerInternalId>>()
        val playerToTeam = mutableMapOf<PlayerInternalId, Int>()
        val teamToWordsGuessed = mutableMapOf<Int, Int>()
        val teamToRoundCount = mutableMapOf<Int, Int>()

        addPlayersToGame(game, playersCount)
        game.players.forEach {
            teamToPlayers.getOrPut(it.teamId) { mutableSetOf() }.add(it.id)
            playerToTeam[it.id] = it.teamId
        }

        addWordsToGame(game, wordsCount)
        game.words.filter { it.status == WordInGameStatus.EXPLAINED }.forEach {
            val teamId = playerToTeam[it.explainerId]!!
            teamToWordsGuessed[teamId] = teamToWordsGuessed.getOrPut(teamId) { 0 } + 1
        }

        addRoundsToGame(game, roundsCount)
        game.rounds.forEach {
            val teamId = playerToTeam[it.explainerId]!!
            teamToRoundCount[teamId] = teamToRoundCount.getOrPut(teamId) { 0 } + 1
        }

        addExplanationToGame(game)

        whenever(gameProvider.findById(game.id)).thenReturn(game)

        val gameReport = usecase.execute(game.id)

        assertEquals(game.words.count { it.isExplained() }, gameReport.wordsGuessed)
        assertEquals(totalTime, gameReport.totalTime)

        assertEquals(playersCount / 2, gameReport.teamStats.size)

        for (teamStat in gameReport.teamStats) {
            assertEquals(teamStat.players.toSet(), teamToPlayers[teamStat.teamId])
            assertEquals(teamStat.wordsGuessed, teamToWordsGuessed[teamStat.teamId])
            assertEquals(teamStat.roundsCount, teamToRoundCount[teamStat.teamId])
        }

        val sortedTeamIds = teamToWordsGuessed.entries
            .sortedByDescending { it.value }
            .map { it.key }
            .toList()
        assertEquals(sortedTeamIds, gameReport.teamStats.map { it.teamId }.toList())
    }

    private fun addPlayersToGame(game: Game, playersCount: Int) {
        assertTrue(playersCount % 2 == 0)
        val teamsCount = playersCount / 2
        for (i in 0 until playersCount) {
            game.players.add(
                Player(
                    id = PlayerInternalId(i.toLong()),
                    userId = UserId(UUID.randomUUID()),
                    gameId = game.id,
                    status = PlayerStatus.READY,
                    role = PlayerRole.PLAYER,
                    teamId = i % teamsCount
                )
            )
        }
    }

    private fun addWordsToGame(game: Game, wordsCount: Int) {
        val playerIds = game.playerIds()
        val playersCount = game.players.size
        for (i in 0 until wordsCount) {
            val status = if (Random().nextInt(0, 100) < 75) {
                WordInGameStatus.EXPLAINED
            } else {
                WordInGameStatus.FUCKUPED
            }
            val authorId = playerIds[i % playersCount]
            val explainerId = playerIds[Random().nextInt(0, playersCount)]
            game.words.add(
                WordInGame(
                    gameId = game.id,
                    value = UUID.randomUUID().toString(),
                    authorId = authorId,
                    status = status,
                    explainerId = explainerId
                )
            )
        }
    }

    private fun addRoundsToGame(game: Game, roundsCount: Int) {
        val playerIds = game.playerIds()
        val playersCount = game.players.size
        for (i in 0 until roundsCount) {
            val playerId = playerIds[i % playersCount]
            game.rounds.add(
                Round(
                    id = RoundId(),
                    explainerId = playerId,
                    gameId = game.id
                )
            )
        }
    }

    private fun addExplanationToGame(game: Game) {
        val roundsCount = game.rounds.size
        game.words.forEach { word ->
            val round = game.rounds[Random().nextInt(0, roundsCount)]
            round.explanations.add(
                Explanation(
                    id = ExplanationId(),
                    roundId = round.id,
                    word = word,
                    result = if (word.status == WordInGameStatus.EXPLAINED) {
                        ExplanationResult.EXPLAINED
                    } else {
                        ExplanationResult.FAILED
                    }
                )
            )
        }
    }

    private fun Game.playerIds() = players.map { it.id }.toList()
}
