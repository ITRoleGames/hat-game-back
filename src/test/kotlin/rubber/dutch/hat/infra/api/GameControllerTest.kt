package rubber.dutch.hat.infra.api

import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import rubber.dutch.hat.BaseApplicationTest
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.GameDto
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.domain.GameConfigProperties
import rubber.dutch.hat.infra.api.dto.ErrorCode
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import java.util.UUID.randomUUID

class GameControllerTest : BaseApplicationTest() {

  @Autowired
  private lateinit var gameConfigProperties: GameConfigProperties

  @Test
  fun `create game success`() {
    val userId = randomUUID()
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
  fun `join game success`() {
    val userId = randomUUID()
    val mockResponse = callCreateGame(CreateGameRequestPayload(userId, 10, 30))
      .andExpect {
        status { isOk() }
        content { contentType(MediaType.APPLICATION_JSON) }
        content { json("{}") }
      }.andReturn().response
    val createGameResponse: GameDto = objectMapper.readValue(mockResponse.contentAsString)

    callJoinGame(JoinGameRequestPayload(code = createGameResponse.code, userId = userId))
      .andExpect {
        status { isOk() }
        jsonPath("id") { isNotEmpty() }
        jsonPath("code") { isNotEmpty() }
        jsonPath("wordsPerPlayer") { value(10) }
        jsonPath("moveTime") { value(30) }
      }
  }

  @Test
  fun `join unknown game failed`() {
    val mockResponse = callJoinGame(JoinGameRequestPayload("unknown_game_code", randomUUID()))
      .andExpect {
        status { isUnprocessableEntity() }
      }.andReturn().response

    val errorResponse: ErrorResponse = objectMapper.readValue(mockResponse.contentAsString)
    assert(errorResponse.code == ErrorCode.GAME_NOT_FOUND)
  }

  @Test
  fun `join game players limit exceeded`() {
    val userId = randomUUID()
    val createGameMockResponse = callCreateGame(CreateGameRequestPayload(userId, 10, 30))
      .andExpect {
        status { isOk() }
        content { contentType(MediaType.APPLICATION_JSON) }
        content { json("{}") }
      }.andReturn().response
    val createGameResponse: GameDto = objectMapper.readValue(createGameMockResponse.contentAsString)

    repeat(gameConfigProperties.maxPlayers - 1) {
      callJoinGame(JoinGameRequestPayload(code = createGameResponse.code, userId = randomUUID()))
        .andExpect {
          status { isOk() }
        }
    }

    val joinGameMockResponse =
      callJoinGame(JoinGameRequestPayload(code = createGameResponse.code, userId = randomUUID()))
        .andExpect {
          status { isUnprocessableEntity() }
        }.andReturn().response

    val errorResponse: ErrorResponse = objectMapper.readValue(joinGameMockResponse.contentAsString)
    assert(errorResponse.code == ErrorCode.PLAYERS_LIMIT_EXCEEDED)
  }

  private fun callCreateGame(request: CreateGameRequestPayload): ResultActionsDsl {
    return mockMvc.post("/api/v1/games") {
      content = objectMapper.writeValueAsString(request)
      contentType = MediaType.APPLICATION_JSON
    }
  }

  private fun callJoinGame(request: JoinGameRequestPayload): ResultActionsDsl {
    return mockMvc.post("/api/v1/game/join") {
      content = objectMapper.writeValueAsString(request)
      contentType = MediaType.APPLICATION_JSON
    }
  }

}