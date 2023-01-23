package rubber.dutch.hat.infra.api

import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import rubber.dutch.hat.BaseApplicationTest
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.domain.GameConfigProperties
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.infra.api.dto.ErrorCode
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import java.util.UUID.randomUUID

class GameControllerTest : BaseApplicationTest() {

  @Autowired
  private lateinit var gameConfigProperties: GameConfigProperties

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
  fun `WHEN join to unknown game THEN error`() {
    val mockResponse = callJoinGame(JoinGameRequestPayload("unknown_game_code", UserId(randomUUID())))
      .andExpect {
        status { isUnprocessableEntity() }
      }.andReturn().response

    val errorResponse: ErrorResponse = objectMapper.readValue(mockResponse.contentAsString)
    assert(errorResponse.code == ErrorCode.GAME_NOT_FOUND)
  }

  @Test
  fun `WHEN join game and players limit exceeded THEN error`() {
    val userId = UserId(randomUUID())
    val gameDto = createGame(userId)

    repeat(gameConfigProperties.maxPlayers - 1) {
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
    assert(errorResponse.code == ErrorCode.PLAYERS_LIMIT_EXCEEDED)
  }

}