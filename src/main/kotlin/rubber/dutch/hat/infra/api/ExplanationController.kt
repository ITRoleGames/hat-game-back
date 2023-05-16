package rubber.dutch.hat.infra.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rubber.dutch.hat.app.FinishCurrentAndStartNewExplanationUsecase
import rubber.dutch.hat.app.dto.ExplanationResponse
import rubber.dutch.hat.app.dto.UpdateExplanationPayload
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.RoundId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import rubber.dutch.hat.infra.api.util.USER_ID_HEADER

@RestController
@RequestMapping("/api/v1")
class ExplanationController(
    private val finishCurrentAndStartNewExplanationUsecase: FinishCurrentAndStartNewExplanationUsecase
) {

    @Operation(
        summary = "Завершить текущее объяснение и начать новое",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Получено новое объяснение"
            ),
            ApiResponse(
                responseCode = "422",
                description = "Бизнес-ошибка",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/games/{gameId}/rounds/{roundId}/finishCurrentAndStartNewExplanation")
    fun finishCurrentAndStartNewExplanation(
        @PathVariable gameId: GameId,
        @PathVariable roundId: RoundId,
        @RequestBody updateExplanationPayload: UpdateExplanationPayload,
        @RequestHeader(USER_ID_HEADER) userId: UserId
    ): ResponseEntity<ExplanationResponse> {
        return finishCurrentAndStartNewExplanationUsecase.execute(
            gameId,
            roundId,
            updateExplanationPayload,
            userId
        )?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.noContent().build()
    }
}
