package rubber.dutch.hat.domain.model.event

import java.util.*

data class GameUpdatedEvent(override val gameId: UUID) : GameEvent {

    override val type = GameEventType.GAME_UPDATED
}