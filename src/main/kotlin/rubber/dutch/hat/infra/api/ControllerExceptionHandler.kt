package rubber.dutch.hat.infra.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import rubber.dutch.hat.domain.exception.*
import rubber.dutch.hat.infra.api.dto.ErrorCode
import rubber.dutch.hat.infra.api.dto.ErrorResponse

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(GameNotFoundException::class)
    fun handleGameNotFoundException(ex: GameNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.GAME_NOT_FOUND))
    }

    @ExceptionHandler(PlayersLimitExceededException::class)
    fun handlePlayersLimitExceededException(ex: PlayersLimitExceededException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.PLAYERS_LIMIT_EXCEEDED))
    }

    @ExceptionHandler(UserNotJoinedException::class)
    fun handleUserNotJoinedException(ex: UserNotJoinedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.USER_NOT_JOINED))
    }

    @ExceptionHandler(WordsLimitExceededException::class)
    fun handleWordsLimitExceededException(ex: WordsLimitExceededException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.WORDS_LIMIT_EXCEEDED))
    }

    @ExceptionHandler(GameStatusException::class)
    fun handleWordsLimitExceededException(ex: GameStatusException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.ILLEGAL_GAME_STATUS_FOR_OPERATION))
    }

    @ExceptionHandler(OperationNotPermittedException::class)
    fun handleWordsLimitExceededException(ex: OperationNotPermittedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.OPERATION_NOT_PERMITTED))
    }
}
