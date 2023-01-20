package rubber.dutch.hat.infra.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.exception.PlayersLimitExceededException
import rubber.dutch.hat.infra.api.dto.ErrorCode
import rubber.dutch.hat.infra.api.dto.ErrorResponse

@ControllerAdvice
class ControllerExceptionHandler {

  @ExceptionHandler(GameNotFoundException::class)
  fun handlePlaceInfoNotFoundException(ex: GameNotFoundException): ResponseEntity<ErrorResponse> {
    return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.GAME_NOT_FOUND))
  }

  @ExceptionHandler(PlayersLimitExceededException::class)
  fun handlePlaceInfoNotFoundException(ex: PlayersLimitExceededException): ResponseEntity<ErrorResponse> {
    return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.PLAYERS_LIMIT_EXCEEDED))
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException::class)
  fun handleInvalidArgumentException(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
    return ResponseEntity.badRequest().body(ErrorResponse(ErrorCode.BAD_REQUEST))
  }

}