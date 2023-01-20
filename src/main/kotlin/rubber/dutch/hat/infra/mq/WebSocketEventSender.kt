package rubber.dutch.hat.infra.mq

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import rubber.dutch.hat.domain.model.event.GameEvent
import rubber.dutch.hat.domain.port.EventSender

@Component
class WebSocketEventSender(private val messagingTemplate: SimpMessagingTemplate) : EventSender {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(WebSocketEventSender::class.java)
    }

    override fun send(event: GameEvent) {
        val destination = "/topic/game/${event.gameId}"
        logger.debug("Sending WS event: $event")
        messagingTemplate.convertAndSend(destination, event)
    }
}