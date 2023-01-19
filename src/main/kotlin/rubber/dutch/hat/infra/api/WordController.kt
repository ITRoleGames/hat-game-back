package rubber.dutch.hat.infra.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import rubber.dutch.hat.app.AddWordsUsecase
import rubber.dutch.hat.app.dto.AddWordsRequestPayload
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import java.util.*

const val USER_ID_HEADER = "user-id"

@RestController
class WordController(
  private val addWordsUsecase: AddWordsUsecase
) {

  @Operation(
    summary = "Добавить слова в шапку",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Слова добавлены"
      ),
      ApiResponse(
        responseCode = "422",
        description = "Бизнес-ошибка",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
      )]
  )
  @PostMapping("/api/v1/words")
  fun addWords(
    @RequestHeader(USER_ID_HEADER) userId: UUID,
    @RequestBody payload: AddWordsRequestPayload
  ) {
    addWordsUsecase.execute(userId, payload)
  }

}