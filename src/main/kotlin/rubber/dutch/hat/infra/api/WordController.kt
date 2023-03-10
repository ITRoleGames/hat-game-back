package rubber.dutch.hat.infra.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*
import rubber.dutch.hat.app.AddWordsUsecase
import rubber.dutch.hat.app.dto.AddWordsRequestPayload
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import rubber.dutch.hat.infra.api.util.USER_ID_HEADER

@RestController
@RequestMapping("/api/v1")
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
    @PostMapping("/words")
    fun addWords(
        @RequestHeader(USER_ID_HEADER) userId: UserId,
        @RequestBody payload: AddWordsRequestPayload
    ) {
        addWordsUsecase.execute(userId, payload)
    }

}
