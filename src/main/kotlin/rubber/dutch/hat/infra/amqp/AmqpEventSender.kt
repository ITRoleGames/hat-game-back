package rubber.dutch.hat.infra.amqp

import org.springframework.amqp.core.AmqpTemplate
import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.port.EventSender
import rubber.dutch.hat.game.api.GameEvent

@Component
class AmqpEventSender(private val amqpTemplate: AmqpTemplate) : EventSender {

    override fun send(event: GameEvent) {
        amqpTemplate.convertAndSend(AmqpConfig.GAME_EVENT_QUEUE_NAME, event)
    }
}
