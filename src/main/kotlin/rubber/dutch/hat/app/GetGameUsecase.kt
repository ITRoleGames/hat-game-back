package rubber.dutch.hat.app

import org.springframework.stereotype.Component
import rubber.dutch.hat.app.dto.GameDto
import rubber.dutch.hat.app.dto.toDto
import rubber.dutch.hat.domain.exception.GameNotFoundException
import rubber.dutch.hat.domain.service.GameProvider
import java.util.*

@Component
class GetGameUsecase(private val gameProvider: GameProvider) {

    fun execute(gameId: UUID, currentUserId: UUID): GameDto {

        val game = gameProvider.findById(gameId) ?: throw GameNotFoundException()

        game.users.firstOrNull() { userId -> userId == currentUserId } ?: throw GameNotFoundException()

        return game.toDto()
    }
}