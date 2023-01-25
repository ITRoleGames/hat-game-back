package rubber.dutch.hat.config

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitMqConfig @Autowired constructor(private val cachingConnectionFactory: CachingConnectionFactory) {

    @Value("\${app.amqp.gameEvent.queue}")
    lateinit var queueName: String

    @Value("\${app.amqp.gameEvent.exchange}")
    lateinit var exchangeName: String

    @Bean
    fun gameEventQueue(): Queue = QueueBuilder.durable(queueName).build()

    @Bean
    fun gameEventExchange(): DirectExchange = DirectExchange(exchangeName)

    @Bean
    fun converter(): Jackson2JsonMessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(converter: Jackson2JsonMessageConverter): RabbitTemplate {
        val template = RabbitTemplate(cachingConnectionFactory)
        template.messageConverter = converter
        return template
    }
}