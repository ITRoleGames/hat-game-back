package rubber.dutch.hat.infra.api

import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import rubber.dutch.hat.BaseApplicationTest
import rubber.dutch.hat.app.dto.AddWordsRequestPayload
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.infra.api.dto.ErrorCode
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import java.util.UUID.randomUUID

class WordsControllerTest : BaseApplicationTest() {

  @Test
  fun `WHEN add words THEN success`() {
    val userId = UserId(randomUUID())
    val gameDto = createGame(userId)

    callAddWords(
      userId,
      AddWordsRequestPayload(
        gameDto.id,
        generateSequence { randomUUID().toString() }.take(gameDto.wordsPerPlayer).toList()
      )
    ).andExpect {
      status { isOk() }
    }
  }

  @Test
  fun `WHEN add words from user not joined THEN error`() {
    val userId = UserId(randomUUID())
    val gameDto = createGame(userId)

    val mockResponse = callAddWords(
      UserId(randomUUID()),
      AddWordsRequestPayload(
        gameDto.id,
        generateSequence { randomUUID().toString() }.take(gameDto.wordsPerPlayer).toList()
      )
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
      userId,
      AddWordsRequestPayload(
        gameDto.id,
        generateSequence { randomUUID().toString() }.take(gameDto.wordsPerPlayer + 1).toList()
      )
    ).andExpect {
      status { isUnprocessableEntity() }
    }.andReturn().response

    val errorResponse: ErrorResponse = objectMapper.readValue(mockResponse.contentAsString)
    assert(errorResponse.code == ErrorCode.WORDS_LIMIT_EXCEEDED)
  }

}
