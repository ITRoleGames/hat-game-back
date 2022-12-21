package rubber.dutch.hat.infra.api.dto

class ErrorResponse(val code: ErrorCode)

enum class ErrorCode {
  GAME_NOT_FOUND,
  PLAYERS_LIMIT_EXCEEDED
}