package rubber.dutch.hat.infra.amqp

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Queue
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AmqpConfig {

    companion object {
        const val GAME_EVENT_QUEUE_NAME = "game-update-event"
    }

    @Bean
    fun queue(): Queue {
        return Queue(GAME_EVENT_QUEUE_NAME, false)
    }

    @Bean
    fun jackson2MessageConverter(objectMapper : ObjectMapper): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }
}

