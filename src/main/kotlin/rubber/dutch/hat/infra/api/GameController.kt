package rubber.dutch.hat.infra.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*
import rubber.dutch.hat.app.*
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.GameReport
import rubber.dutch.hat.app.dto.GameResponse
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.domain.model.GameId
import rubber.dutch.hat.domain.model.UserId
import rubber.dutch.hat.infra.api.dto.ErrorResponse
import rubber.dutch.hat.infra.api.util.USER_ID_HEADER
import java.util.*

@RestController
@RequestMapping("/api/v1")
class GameController(
    private val getGameUsecase: GetGameUsecase,
    private val createGameUsecase: CreateGameUsecase,
    private val joinGameUsecase: JoinGameUsecase,
    private val startGameUsecase: StartGameUsecase,
    private val getGameReportUsecase: GetGameReportUsecase
) {
    @Operation(
        summary = "Получить игру по ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Найденная игра"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные параметры запроса",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "Бизнес-ошибка",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/games/{id}")
    fun getGame(@PathVariable id: UUID, @RequestHeader(USER_ID_HEADER) currentUserId: UUID): GameResponse {
        return getGameUsecase.execute(GameId(id), UserId(currentUserId))
    }

    @Operation(
        summary = "Создать игру",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Игра создана"
            ),
            ApiResponse(
                responseCode = "422",
                description = "Бизнес-ошибка",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/games")
    fun createGame(
        @RequestBody payload: CreateGameRequestPayload
    ): GameResponse {
        return createGameUsecase.execute(payload)
    }

    @Operation(
        summary = "Присоединить пользователя к игре",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь присоединён к игре"
            ),
            ApiResponse(
                responseCode = "422",
                description = "Бизнес-ошибка",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/game/join")
    fun joinGame(
        @RequestBody payload: JoinGameRequestPayload
    ): GameResponse {
        return joinGameUsecase.execute(payload)
    }

    @Operation(
        summary = "Старт игры",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Игра начата"
            ),
            ApiResponse(
                responseCode = "422",
                description = "Бизнес-ошибка",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/games/{gameId}/startGame")
    fun startGame(
        @PathVariable gameId: GameId,
        @RequestHeader(USER_ID_HEADER) currentUserId: UserId
    ): GameResponse {
        return startGameUsecase.execute(gameId, currentUserId)
    }

    @Operation(
        summary = "Получить отчёт об игре",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Успешный ответ"
            ),
            ApiResponse(
                responseCode = "422",
                description = "Бизнес-ошибка",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/games/{gameId}/report")
    fun getGameReport(
        @PathVariable gameId: GameId,
    ): GameReport {
        return getGameReportUsecase.execute(gameId)
    }
}
