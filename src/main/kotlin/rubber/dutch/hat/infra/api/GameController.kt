package rubber.dutch.hat.infra.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import rubber.dutch.hat.app.CreateGameUsecase
import rubber.dutch.hat.app.JoinGameUsecase
import rubber.dutch.hat.app.dto.CreateGameRequestPayload
import rubber.dutch.hat.app.dto.CreateGameResponse
import rubber.dutch.hat.app.dto.JoinGameRequestPayload
import rubber.dutch.hat.app.dto.JoinGameResponse
import rubber.dutch.hat.infra.api.dto.ErrorResponse

@RestController
class GameController(
  private val createGameUsecase: CreateGameUsecase,
  private val joinGameUsecase: JoinGameUsecase
) {

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
      )]
  )
  @PostMapping("/api/v1/games")
  fun createGame(
    @RequestBody payload: CreateGameRequestPayload
  ): CreateGameResponse {
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
      )]
  )
  @PostMapping("/api/v1/games/join")
  fun joinGame(
    @RequestBody payload: JoinGameRequestPayload
  ): JoinGameResponse {
    return joinGameUsecase.execute(payload)
  }

}