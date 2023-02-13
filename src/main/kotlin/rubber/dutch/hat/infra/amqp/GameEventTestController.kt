package rubber.dutch.hat.infra.amqp

import org.springframework.amqp.core.AmqpTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import rubber.dutch.hat.domain.model.event.GameUpdatedEvent
import rubber.dutch.hat.infra.amqp.AmqpConfig.Companion.GAME_EVENT_QUEUE_NAME
import java.util.*

@RestController
class GameEventTestController(val amqpTemplate: AmqpTemplate) {

    @GetMapping("/send")
    fun sendEvent(@RequestParam("gameId") gameId: String) {
        amqpTemplate.convertAndSend(GAME_EVENT_QUEUE_NAME, GameUpdatedEvent(UUID.randomUUID()))
    }
}
