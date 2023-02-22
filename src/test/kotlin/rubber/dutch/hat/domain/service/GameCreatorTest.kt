package rubber.dutch.hat.domain.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import rubber.dutch.hat.domain.model.GameConfig
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.port.GameRepository
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GameCreatorTest {

    @Mock
    lateinit var gameRepository: GameRepository

    @InjectMocks
    lateinit var gameCreator: GameCreator

    @Test
    fun createGame() {
        `when`(gameRepository.save(any())).then { it.arguments.first() }
        val game = gameCreator.createGame(
            UserId(UUID.randomUUID()),
            GameConfig(8, 30)
        )
        assertNotNull(game.id)
        assertEquals(5, game.code.length)
        assertTrue("[a-z0-9]{5}".toRegex().matches(game.code))
        assertNotNull(game.creatorId)
        assertEquals(8, game.config.wordsPerPlayer)
        assertEquals(30, game.config.moveTime)
        assertEquals(1, game.players.size)
        assertTrue(game.words.isEmpty())
    }
}
