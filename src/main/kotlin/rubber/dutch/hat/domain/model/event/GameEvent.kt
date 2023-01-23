package rubber.dutch.hat.domain.model.event

import rubber.dutch.hat.domain.model.GameId

interface GameEvent {

    val type: GameEventType

    val gameId: GameId
}