package br.com.twittercomplainer.configuration.rabbitmq

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PostsConfiguration {

    @Bean
    fun queueDlq() = Queue(POSTS_QUEUE_DLQ, true)

    @Bean
    fun exchangeDlq() = DirectExchange(TOPIC_EXCHANGE_DLQ)

    @Bean
    fun bindingDlq() = BindingBuilder.bind(queueDlq()).to(exchangeDlq()).with(ROUTING_KEY_DLQ)

    @Bean
    fun queue() = Queue(POSTS_QUEUE, true).apply {
        addArgument("x-dead-letter-exchange", TOPIC_EXCHANGE_DLQ)
        addArgument("x-dead-letter-exchange-key", ROUTING_KEY_DLQ)
    }

    @Bean
    fun exchange() = DirectExchange(TOPIC_EXCHANGE)

    @Bean
    fun binding() = BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY)

    companion object {
        const val POSTS_QUEUE = "twitter.posts"
        const val POSTS_QUEUE_DLQ = "twitter.posts.dlq"
        const val TOPIC_EXCHANGE = "twitter.posts.exchange"
        const val TOPIC_EXCHANGE_DLQ = "twitter.posts.exchange.dlq"
        const val ROUTING_KEY = "twitter.posts"
        const val ROUTING_KEY_DLQ = "twitter.posts.dlq"
    }
}
