package rubber.dutch.hat.domain.port

import rubber.dutch.hat.domain.model.event.GameEvent

interface EventSender {

    fun send(event: GameEvent)
}