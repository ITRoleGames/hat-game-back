package rubber.dutch.hat.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.service.GameProvider
import rubber.dutch.hat.domain.service.GameSaver
import java.util.*
import java.util.concurrent.ThreadLocalRandom

internal class StartGameUsecaseTest {

    @Test
    fun execute_8_payers_4_teams() {
        val gameProvider = mock(GameProvider::class.java)
        val gameSaver = mock(GameSaver::class.java)

        val userId = UserId(UUID.randomUUID())
        val gameId = GameId(UUID.randomUUID())
        val startGameUsecase = StartGameUsecase(gameProvider, gameSaver)

        val game = getGame(gameId, userId, 8)
        `when`(gameProvider.findById(gameId)).thenReturn(game)
        `when`(gameSaver.saveAndNotify(any())).then { it.arguments.first() }

        val updatedGame = startGameUsecase.execute(gameId, userId)

        assertTrue(updatedGame.players.all { it.teamId != null })
        assertEquals(Game.GameStatus.STARTED, updatedGame.status)
        assertEquals(4, updatedGame.players.map { it.teamId }.distinct().size)
        val playersGroupedByTeam = updatedGame.players.groupBy { it.teamId }
        playersGroupedByTeam.forEach { entry ->
            val players = entry.value.sortedBy { it.moveOrder }
            assertTrue(
                players[0].moveOrder + 4 == players[1].moveOrder,
                "First and second player in a team don't have a gap of 3 players"
            )
        }
    }

    @Test
    fun execute_7_payers_3_teams() {
        val gameProvider = mock(GameProvider::class.java)
        val gameSaver = mock(GameSaver::class.java)

        val gameId = GameId(UUID.randomUUID())
        val userId = UserId(UUID.randomUUID())
        val startGameUsecase = StartGameUsecase(gameProvider, gameSaver)

        val game = getGame(gameId, userId, 7)
        `when`(gameProvider.findById(gameId)).thenReturn(game)
        `when`(gameSaver.saveAndNotify(any())).then { it.arguments.first() }

        val updatedGame = startGameUsecase.execute(gameId, userId)

        assertTrue(updatedGame.players.all { it.teamId != null })
        assertEquals(Game.GameStatus.STARTED, updatedGame.status)
        assertEquals(3, updatedGame.players.groupBy { it.teamId }.entries.map { it.value.size }.max())
        assertEquals(3, updatedGame.players.map { it.teamId }.distinct().size)

        val playersGroupedByTeam = updatedGame.players.groupBy { it.teamId }
        playersGroupedByTeam.forEach { entry ->
            val players = entry.value.sortedBy { it.moveOrder }
            assertTrue(
                players[0].moveOrder + 3 == players[1].moveOrder,
                "First and second player in a team don't have a gap of 3 players"
            )

            if (entry.value.size == 3) {
                assertTrue(
                    players[0].moveOrder + 3 == players[2].moveOrder,
                    "First and third player in a team don't have a gap of 3 players"
                )
            }
        }
    }

    @Test
    fun execute_6_payers_3_teams() {
        val gameProvider = mock(GameProvider::class.java)
        val gameSaver = mock(GameSaver::class.java)

        val userId = UserId(UUID.randomUUID())
        val gameId = GameId(UUID.randomUUID())
        val startGameUsecase = StartGameUsecase(gameProvider, gameSaver)

        val game = getGame(gameId, userId, 6)
        `when`(gameProvider.findById(gameId)).thenReturn(game)
        `when`(gameSaver.saveAndNotify(any())).then { it.arguments.first() }

        val updatedGame = startGameUsecase.execute(gameId, userId)

        assertTrue(updatedGame.players.all { it.teamId != null })
        assertEquals(Game.GameStatus.STARTED, updatedGame.status)
        assertEquals(2, updatedGame.players.groupBy { it.teamId }.entries.map { it.value.size }.max())
        assertEquals(3, updatedGame.players.map { it.teamId }.distinct().size)

        val playersGroupedByTeam = updatedGame.players.groupBy { it.teamId }
        playersGroupedByTeam.forEach { entry ->
            val players = entry.value.sortedBy { it.moveOrder }
            assertTrue(
                players[0].moveOrder + 3 == players[1].moveOrder,
                "First and second player in a team don't have a gap of 3 players"
            )
        }
    }

    private fun getGame(gameId: GameId, creatorId: UserId, playersNum: Int): Game {
        return Game(
            id = gameId,
            code = "test",
            creatorId = creatorId,
            config = mock(GameConfig::class.java),
            players = createPlayers(playersNum, gameId),
            words = mutableListOf()
        )
    }

    private fun createPlayers(numberOfPlayers: Int, gameId: GameId): MutableList<Player> {
        return (1..numberOfPlayers).map {
            Player(
                id = PlayerInternalId(ThreadLocalRandom.current().nextLong()),
                userId = UserId(UUID.randomUUID()),
                gameId = gameId,
                status = PlayerStatus.READY,
                role = PlayerRole.PLAYER,
                words = mutableListOf()
            )
        }.toMutableList()
    }
}
