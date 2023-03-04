package rubber.dutch.hat.infra.amqp

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import rubber.dutch.hat.game.api.GameUpdatedEvent
import rubber.dutch.hat.infra.amqp.AmqpConfig.Companion.GAME_EVENT_QUEUE_NAME

@Component
@Profile(value = ["test"])
class TestGameEventListener {

    @RabbitListener(queues = [GAME_EVENT_QUEUE_NAME])
    fun onGameUpdated(event: GameUpdatedEvent) {
        events.add(event)
    }

    fun getEvents() = events

    fun clearEvents() = events.clear()

    companion object {
        val events = mutableListOf<GameUpdatedEvent>()
    }
}
