package rubber.dutch.hat.domain.model.event

import java.util.*

interface GameEvent {

    val type: GameEventType

    val gameId: UUID
}
