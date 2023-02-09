package rubber.dutch.hat.domain.model.event

import rubber.dutch.hat.domain.model.GameId

data class GameUpdatedEvent(override val gameId: GameId) : GameEvent {

    override val type = GameEventType.GAME_UPDATED
}