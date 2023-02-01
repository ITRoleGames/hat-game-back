package rubber.dutch.hat.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import rubber.dutch.hat.domain.model.*
import rubber.dutch.hat.domain.port.GameSaver
import rubber.dutch.hat.domain.service.GameProvider
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import org.mockito.kotlin.any

internal class StartGameUsecaseTest {

    @Test
    fun `WHEN 8 player 4 teams`() {
        val gameProvider = mock(GameProvider::class.java)
        val gameSaver = mock(GameSaver::class.java)

        val gameId = GameId(UUID.randomUUID())
        val userCase = StartGameUsecase(gameProvider, gameSaver)

        val game = getGame(gameId, 8)
        `when`(gameProvider.findById(gameId)).thenReturn(game)
        `when`(gameSaver.save(any())).then { it.arguments.first() }

        val updatedGame = userCase.execute(gameId)

        assertTrue(updatedGame.players.all { it.teamId != null })
        assertEquals(4, updatedGame.players.map { it.teamId }.distinct().size)
    }

    @Test
    fun `WHEN 7 player 3 teams`() {
        val gameProvider = mock(GameProvider::class.java)
        val gameSaver = mock(GameSaver::class.java)

        val gameId = GameId(UUID.randomUUID())
        val userCase = StartGameUsecase(gameProvider, gameSaver)

        val game = getGame(gameId, 7)
        `when`(gameProvider.findById(gameId)).thenReturn(game)
        `when`(gameSaver.save(any())).then { it.arguments.first() }

        val updatedGame = userCase.execute(gameId)

        assertTrue(updatedGame.players.all { it.teamId != null })
        //one team has 3 players
        assertEquals(3, updatedGame.players.groupBy { it.teamId }.entries.map { it.value.size }.max())
        assertEquals(3, updatedGame.players.map { it.teamId }.distinct().size)
    }

    private fun getGame(gameId: GameId, playersNum: Int): Game {
        return Game(
            id = gameId,
            code = "test",
            creatorId = UserId(UUID.randomUUID()),
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
