package rubber.dutch.hat

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import rubber.dutch.hat.app.dto.AddWordsRequestPayload
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.infra.api.util.USER_ID_HEADER

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class BaseApplicationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    companion object {

        @JvmStatic
        private val postgreSQLContainer = PostgreSQLContainer("postgres:15-alpine")
            .waitingFor(Wait.defaultWaitStrategy())
            .apply { start() }

        @DynamicPropertySource
        @JvmStatic
        fun initProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { "jdbc:tc:postgresql:15:///${postgreSQLContainer.databaseName}" }
            registry.add("spring.datasource.username") { postgreSQLContainer.username }
            registry.add("spring.datasource.password") { postgreSQLContainer.password }
            registry.add("spring.datasource.driverClassName") { "org.testcontainers.jdbc.ContainerDatabaseDriver" }
        }
    }

    protected fun callCreateGame(request: CreateGameRequestPayload): ResultActionsDsl {
        return mockMvc.post("/api/v1/games") {
            content = objectMapper.writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
        }
    }

    protected fun callJoinGame(request: JoinGameRequestPayload): ResultActionsDsl {
        return mockMvc.post("/api/v1/game/join") {
            content = objectMapper.writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
        }
    }

    protected fun createGame(userId: UserId): GameResponse {
        val mockHttpServletResponse = callCreateGame(CreateGameRequestPayload(userId, 10, 30))
            .andExpect {
                status { isOk() }
            }
            .andReturn().response
        return objectMapper.readValue(mockHttpServletResponse.contentAsString, GameResponse::class.java)
    }

    protected fun callGetGame(gameId: GameId, userId: UserId): ResultActionsDsl {
        return mockMvc.get("/api/v1/games/${gameId.gameId}") {
            header(USER_ID_HEADER, userId.userId)
        }
    }
    protected fun callAddWords(userId: UserId, payload: AddWordsRequestPayload): ResultActionsDsl {
        return mockMvc.post("/api/v1/words") {
            header(USER_ID_HEADER, userId.userId)
            content = objectMapper.writeValueAsString(payload)
            contentType = MediaType.APPLICATION_JSON
        }
    }

    protected fun callStartGame(gameId: GameId, userId: UserId): ResultActionsDsl {
        return mockMvc.post("/api/v1/games/${gameId.gameId}/startGame") {
            header(USER_ID_HEADER, userId.userId)
            contentType = MediaType.APPLICATION_JSON
        }
    }
}
