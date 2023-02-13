package rubber.dutch.hat.infra.api

import com.fasterxml.jackson.module.kotlin.readValue
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import rubber.dutch.hat.BaseApplicationTest
import rubber.dutch.hat.app.dto.AddWordsRequestPayload
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.domain.model.event.GameEventType
import rubber.dutch.hat.infra.api.dto.ErrorCode
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import java.util.UUID.randomUUID
import java.util.concurrent.TimeUnit

class WordsControllerTest : BaseApplicationTest() {

    @Test
    fun `WHEN add words THEN success`() {
        val userId = UserId(randomUUID())
        val gameDto = createGame(userId)

        callAddWords(
            userId, generateRandomWordsPayload(gameDto.id, gameDto.wordsPerPlayer)
        ).andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `WHEN add words THEN GameEvent sent`() {
        val userId = UserId(randomUUID())
        val gameDto = createGame(userId)

        addRandomWords(userId, gameDto.id, gameDto.wordsPerPlayer)

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until { gameEventListener.getEvents().size > 0 }
        assert(gameEventListener.getEvents().size == 1)

        assert(gameEventListener.getEvents()[0].type == GameEventType.GAME_UPDATED)
        assert(gameEventListener.getEvents()[0].gameId == gameDto.id.gameId)
    }

    @Test
    fun `WHEN add words from user not joined THEN error`() {
        val userId = UserId(randomUUID())
        val gameDto = createGame(userId)

        val mockResponse = callAddWords(
            UserId(randomUUID()), generateRandomWordsPayload(gameDto.id, gameDto.wordsPerPlayer)
        ).andExpect {
            status { isUnprocessableEntity() }
        }.andReturn().response

        val errorResponse: ErrorResponse = objectMapper.readValue(mockResponse.contentAsString)
        assert(errorResponse.code == ErrorCode.USER_NOT_JOINED)
    }

    @Test
    fun `WHEN add words more than wordsPerPlayer THEN error`() {
        val userId = UserId(randomUUID())
        val gameDto = createGame(userId)

        val mockResponse = callAddWords(
            userId, generateRandomWordsPayload(gameDto.id, gameDto.wordsPerPlayer + 1)
        ).andExpect {
            status { isUnprocessableEntity() }
        }.andReturn().response

        val errorResponse: ErrorResponse = objectMapper.readValue(mockResponse.contentAsString)
        assert(errorResponse.code == ErrorCode.WORDS_LIMIT_EXCEEDED)
    }

    private fun addRandomWords(userId: UserId, gameId: GameId, wordsCount: Int) {
        callAddWords(userId, generateRandomWordsPayload(gameId, wordsCount))
    }

    private fun generateRandomWordsPayload(gameId: GameId, wordsCount: Int): AddWordsRequestPayload {
        return AddWordsRequestPayload(
            gameId,
            generateSequence { randomUUID().toString() }.take(wordsCount).toList()
        )
    }

    private fun callAddWords(userId: UserId, payload: AddWordsRequestPayload): ResultActionsDsl {
        return mockMvc.post("/api/v1/words") {
            header(USER_ID_HEADER, userId.userId)
            content = objectMapper.writeValueAsString(payload)
            contentType = MediaType.APPLICATION_JSON
        }
    }

}
