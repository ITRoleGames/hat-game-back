package rubber.dutch.hat.infra.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rubber.dutch.hat.app.CreateRoundUsecase
import rubber.dutch.hat.app.FinishRoundUsecase
import rubber.dutch.hat.app.dto.RoundResponse
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.RoundId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import rubber.dutch.hat.infra.api.util.USER_ID_HEADER

@RestController
@RequestMapping("/api/v1")
class RoundController(
    private val createRoundUsecase: CreateRoundUsecase,
    private val finishRoundUsecase: FinishRoundUsecase
) {

    @Operation(
        summary = "Создать раунд",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Раунд создан"
            ),
            ApiResponse(
                responseCode = "422",
                description = "Бизнес-ошибка",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/games/{gameId}/rounds")
    fun createRound(
        @PathVariable gameId: GameId,
        @RequestHeader(USER_ID_HEADER) userId: UserId
    ): RoundResponse {
        return createRoundUsecase.execute(gameId, userId)
    }

    @Operation(
        summary = "Закончить раунд",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Раунд завершен"
            ),
            ApiResponse(
                responseCode = "422",
                description = "Бизнес-ошибка",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/games/{gameId}/rounds/{roundId}/finish")
    fun finishRound(
        @PathVariable gameId: GameId,
        @PathVariable roundId: RoundId,
        @RequestHeader(USER_ID_HEADER) userId: UserId
    ) {
        finishRoundUsecase.execute(gameId, userId, roundId)
    }
}
