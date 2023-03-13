package rubber.dutch.hat.domain.port

import rubber.dutch.hat.game.api.GameEvent

interface EventSender {

    fun send(event: GameEvent)
}
