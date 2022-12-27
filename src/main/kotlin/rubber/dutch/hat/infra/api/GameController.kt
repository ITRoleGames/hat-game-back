package rubber.dutch.hat.infra.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import rubber.dutch.hat.app.CreateGameUsecase
import rubber.dutch.hat.app.JoinGameUsecase
import rubber.dutch.hat.app.dto.CreateGameRequest
import rubber.dutch.hat.app.dto.CreateGameResponse
import rubber.dutch.hat.app.dto.JoinGameRequest
import rubber.dutch.hat.app.dto.JoinGameResponse

@RestController
class GameController(
  private val createGameUsecase: CreateGameUsecase,
  private val joinGameUsecase: JoinGameUsecase
) {

  @Operation(
    summary = "Создать игру",
    responses = [
      ApiResponse(
        responseCode = "200", description = "Игра создана"),
      ApiResponse(
        responseCode = "422", description = "Бизнес-ошибка"
      )]
  )
  @PostMapping("/api/v1/games")
  fun createGame(
    @RequestBody request: CreateGameRequest
  ): CreateGameResponse {
    return createGameUsecase.execute(request)
  }

  @Operation(
    summary = "Присоединить пользователя к игре",
    responses = [
      ApiResponse(
        responseCode = "200", description = "Пользователь присоединён к игре"),
      ApiResponse(
        responseCode = "422", description = "Бизнес-ошибка"
      )]
  )
  @PostMapping("/api/v1/games/join")
  fun joinGame(
    @RequestBody request: JoinGameRequest
  ): JoinGameResponse {
    return joinGameUsecase.execute(request)
  }

}