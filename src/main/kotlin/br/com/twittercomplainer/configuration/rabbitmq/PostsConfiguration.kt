package br.com.twittercomplainer.configuration.rabbitmq

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.QueueBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PostsConfiguration {

    @Bean
    fun queueDlq() = QueueBuilder.durable(QUEUE_DLQ).build()

    @Bean
    fun exchangeDlq() = DirectExchange(EXCHANGE_DLQ)

    @Bean
    fun bindingDlq() = BindingBuilder.bind(queueDlq()).to(exchangeDlq()).with(QUEUE_DLQ)

    @Bean
    fun queue() = QueueBuilder.durable(QUEUE)
        .withArgument("x-dead-letter-exchange", EXCHANGE_DLQ)
        .withArgument("x-dead-letter-routing-key", QUEUE_DLQ)
        .build()

    @Bean
    fun exchange() = DirectExchange(EXCHANGE)

    @Bean
    fun binding() = BindingBuilder.bind(queue()).to(exchange()).with(QUEUE)

    companion object {
        const val QUEUE = "twitter.posts"
        const val QUEUE_DLQ = QUEUE + ".dlq"
        const val EXCHANGE = "twitter.posts.exchange"
        const val EXCHANGE_DLQ = EXCHANGE + ".dlx"
    }
}
