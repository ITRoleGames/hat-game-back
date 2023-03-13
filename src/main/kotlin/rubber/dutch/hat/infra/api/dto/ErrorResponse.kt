package rubber.dutch.hat.infra.api.dto

class ErrorResponse(val code: ErrorCode)

enum class ErrorCode {
  GAME_NOT_FOUND,
  PLAYERS_LIMIT_EXCEEDED,
  USER_NOT_JOINED,
  WORDS_LIMIT_EXCEEDED,
  INVALID_GAME_STATUS,
  PLAYER_NOT_FOUND,
  INVALID_ROUND_STATUS,
  OPERATION_NOT_PERMITTED
}
