package rubber.dutch.hat.infra.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*
import rubber.dutch.hat.app.AddRoundUsecase
import rubber.dutch.hat.app.dto.RoundDto
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import rubber.dutch.hat.infra.api.util.USER_ID_HEADER

@RestController
@RequestMapping("/api/v1")
class RoundController(private val addRoundUsecase: AddRoundUsecase) {

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
    fun startRound(
        @PathVariable gameId: GameId,
        @RequestHeader(USER_ID_HEADER) userId: UserId
    ): RoundDto {
        return addRoundUsecase.execute(gameId, userId)
    }
}
