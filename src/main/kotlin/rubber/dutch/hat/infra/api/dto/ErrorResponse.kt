package rubber.dutch.hat.infra.api.dto

class ErrorResponse(val code: ErrorCode)

enum class ErrorCode {
    GAME_NOT_FOUND,
    PLAYERS_LIMIT_EXCEEDED,
    USER_NOT_JOINED,
    WORDS_LIMIT_EXCEEDED,
    ILLEGAL_GAME_STATUS_FOR_OPERATION,
    OPERATION_NOT_PERMITTED
}
