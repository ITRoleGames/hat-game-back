package rubber.dutch.hat.infra.api

import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import rubber.dutch.hat.BaseApplicationTest
import rubber.dutch.hat.app.dto.CreateGameRequest
import rubber.dutch.hat.app.dto.CreateGameResponse
import rubber.dutch.hat.app.dto.JoinGameRequest
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
    callCreateGame(CreateGameRequest(userId, 10, 30))
      .andExpect {
        status { isOk() }
        content { contentType(MediaType.APPLICATION_JSON) }
        content { json("{}") }
      }
  }

  @Test
  fun `join game success`() {
    val userId = randomUUID()
    val mockResponse = callCreateGame(CreateGameRequest(userId, 10, 30))
      .andExpect {
        status { isOk() }
        content { contentType(MediaType.APPLICATION_JSON) }
        content { json("{}") }
      }.andReturn().response
    val createGameResponse: CreateGameResponse = objectMapper.readValue(mockResponse.contentAsString)

    callJoinGame(JoinGameRequest(code = createGameResponse.code, userId = userId))
      .andExpect {
        status { isOk() }
      }
  }

  @Test
  fun `join unknown game failed`() {
    val mockResponse = callJoinGame(JoinGameRequest("unknown_game_code", randomUUID()))
      .andExpect {
        status { isUnprocessableEntity() }
      }.andReturn().response

    val errorResponse: ErrorResponse = objectMapper.readValue(mockResponse.contentAsString)
    assert(errorResponse.code == ErrorCode.GAME_NOT_FOUND)
  }

  @Test
  fun `join game players limit exceeded`() {
    val userId = randomUUID()
    val createGameMockResponse = callCreateGame(CreateGameRequest(userId, 10, 30))
      .andExpect {
        status { isOk() }
        content { contentType(MediaType.APPLICATION_JSON) }
        content { json("{}") }
      }.andReturn().response
    val createGameResponse: CreateGameResponse = objectMapper.readValue(createGameMockResponse.contentAsString)

    repeat(gameConfigProperties.maxPlayers - 1) {
      callJoinGame(JoinGameRequest(code = createGameResponse.code, userId = randomUUID()))
        .andExpect {
          status { isOk() }
        }
    }

    val joinGameMockResponse =
      callJoinGame(JoinGameRequest(code = createGameResponse.code, userId = randomUUID()))
        .andExpect {
          status { isUnprocessableEntity() }
        }.andReturn().response

    val errorResponse: ErrorResponse = objectMapper.readValue(joinGameMockResponse.contentAsString)
    assert(errorResponse.code == ErrorCode.PLAYERS_LIMIT_EXCEEDED)
  }

  private fun callCreateGame(request: CreateGameRequest): ResultActionsDsl {
    return mockMvc.post("/api/v1/games") {
      content = objectMapper.writeValueAsString(request)
      contentType = MediaType.APPLICATION_JSON
    }
  }

  private fun callJoinGame(request: JoinGameRequest): ResultActionsDsl {
    return mockMvc.post("/api/v1/games/join") {
      content = objectMapper.writeValueAsString(request)
      contentType = MediaType.APPLICATION_JSON
    }
  }

}