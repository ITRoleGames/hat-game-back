package rubber.dutch.hat.infra.api

import com.fasterxml.jackson.module.kotlin.readValue
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import rubber.dutch.hat.BaseApplicationTest
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.domain.model.MAX_PLAYERS_COUNT
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.game.api.GameEventType
import rubber.dutch.hat.infra.api.dto.ErrorCode
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import java.util.UUID.randomUUID
import java.util.concurrent.TimeUnit

class GameControllerTest : BaseApplicationTest() {

    @Test
    fun `WHEN get game THAN success`() {
        val userId = UserId(randomUUID())

        val mockResponse = callCreateGame(CreateGameRequestPayload(userId, 10, 30))
            .andReturn().response
        val createGameResponse: GameResponse = objectMapper.readValue(mockResponse.contentAsString)

        callGetGame(createGameResponse.id, userId)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("id") { value(createGameResponse.id.gameId.toString()) }
                jsonPath("code") { value(createGameResponse.code) }
                jsonPath("wordsPerPlayer") { value(10) }
                jsonPath("moveTime") { value(30) }
            }
    }

    @Test
    fun `WHEN create game THAN success`() {
        val userId = UserId(randomUUID())
        callCreateGame(CreateGameRequestPayload(userId, 10, 30))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("id") { isNotEmpty() }
                jsonPath("code") { isNotEmpty() }
                jsonPath("wordsPerPlayer") { value(10) }
                jsonPath("moveTime") { value(30) }
            }
    }

    @Test
    fun `WHEN join game THAN success`() {
        val userId = UserId(randomUUID())
        val gameDto = createGame(userId)

        callJoinGame(JoinGameRequestPayload(code = gameDto.code, userId = userId))
            .andExpect {
                status { isOk() }
                jsonPath("id") { isNotEmpty() }
                jsonPath("code") { isNotEmpty() }
                jsonPath("wordsPerPlayer") { value(10) }
                jsonPath("moveTime") { value(30) }
            }
    }

    @Test
    fun `WHEN join game THAN GameEvent sent`() {
        val userId = UserId(randomUUID())
        val gameDto = createGame(userId)

        joinGame(JoinGameRequestPayload(code = gameDto.code, userId = userId))

        await().atMost(10, TimeUnit.SECONDS).until { gameEventListener.getEvents().size > 0 }
        assertEquals(1, gameEventListener.getEvents().size)

        assertEquals(GameEventType.GAME_UPDATED, gameEventListener.getEvents()[0].type)
        assertEquals(gameDto.id.gameId, gameEventListener.getEvents()[0].gameId)
    }

    @Test
    fun `WHEN join to unknown game THEN error`() {
        val mockResponse = callJoinGame(JoinGameRequestPayload("unknown_game_code", UserId(randomUUID())))
            .andExpect {
                status { isUnprocessableEntity() }
            }.andReturn().response

        val errorResponse: ErrorResponse = objectMapper.readValue(mockResponse.contentAsString)
        assertEquals(ErrorCode.GAME_NOT_FOUND, errorResponse.code)
    }

    @Test
    fun `WHEN join game and players limit exceeded THEN error`() {
        val userId = UserId(randomUUID())
        val gameDto = createGame(userId)

        repeat(MAX_PLAYERS_COUNT - 1) {
            callJoinGame(JoinGameRequestPayload(code = gameDto.code, userId = UserId(randomUUID())))
                .andExpect {
                    status { isOk() }
                }
        }

        val joinGameMockResponse =
            callJoinGame(JoinGameRequestPayload(code = gameDto.code, userId = UserId(randomUUID())))
                .andExpect {
                    status { isUnprocessableEntity() }
                }.andReturn().response

        val errorResponse: ErrorResponse = objectMapper.readValue(joinGameMockResponse.contentAsString)
        assertEquals(ErrorCode.PLAYERS_LIMIT_EXCEEDED, errorResponse.code)
    }
}
