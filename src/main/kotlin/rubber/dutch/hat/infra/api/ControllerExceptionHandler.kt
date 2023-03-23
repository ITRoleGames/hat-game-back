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
    fun handleGameStatusException(ex: GameStatusException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.INVALID_GAME_STATUS))
    }

    @ExceptionHandler(RoundStatusException::class)
    fun handleRoundStatusException(ex: RoundStatusException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.INVALID_ROUND_STATUS))
    }

    @ExceptionHandler(PlayerNotFoundException::class)
    fun handlePlayerNotFoundException(ex: PlayerNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.PLAYER_NOT_FOUND))
    }

    @ExceptionHandler(OperationNotPermittedException::class)
    fun handleOperationNotPermittedException(ex: OperationNotPermittedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.OPERATION_NOT_PERMITTED))
    }

    @ExceptionHandler(ExplanationNotFoundException::class)
    fun handleExplanationNotFoundException(ex: ExplanationNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.EXPLANATION_NOT_FOUND))
    }

    @ExceptionHandler(MoveOrderException::class)
    fun handleMoveOrderException(ex: MoveOrderException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.INVALID_MOVE_ORDER))
    }

    @ExceptionHandler(RoundNotFoundException::class)
    fun handleRoundNotFoundException(ex: RoundNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.ROUND_NOT_FOUND))
    }

    @ExceptionHandler(ExplanationResultException::class)
    fun handleExplanationResultException(ex: ExplanationResultException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.unprocessableEntity().body(ErrorResponse(ErrorCode.INVALID_EXPLANATION_RESULT))
    }
}
