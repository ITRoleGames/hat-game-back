package rubber.dutch.hat.infra.mq

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.event.GameEvent
import rubber.dutch.hat.domain.port.EventSender

@Component
class RabbitMqEventSender @Autowired constructor(
        private val rabbitTemplate: RabbitTemplate,
        private val gameEventQueue: Queue,
) : EventSender {

    override fun send(event: GameEvent) {

        rabbitTemplate.convertAndSend(gameEventQueue.name, event)
    }
}