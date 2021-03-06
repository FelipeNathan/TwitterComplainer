package br.com.twittercomplainer.configuration.rabbitmq

import java.net.URI
import java.time.Duration
import java.time.temporal.ChronoUnit
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.interceptor.RetryOperationsInterceptor

@Configuration
class RabbitMQConfiguration(
    @Value("\${rabbitmq.url}") private var rabbitUrl: String
) {

    @Bean
    fun connectionFactory() = CachingConnectionFactory(URI(rabbitUrl))

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory) = RabbitAdmin(connectionFactory)

    @Bean
    fun retryOperationsInterceptor(): RetryOperationsInterceptor = RetryInterceptorBuilder
        .stateless()
        .maxAttempts(MAX_ATTEMPTS)
        .backOffOptions(INITIAL_INTERVAL, MULTIPLIER, MAX_INTERVAL)
        .recoverer(RejectAndDontRequeueRecoverer())
        .build()

    @Bean
    fun rabbitListenerContainerFactory() =
        SimpleRabbitListenerContainerFactory().apply {
            setConnectionFactory(connectionFactory())
            setAdviceChain(retryOperationsInterceptor())
        }

    companion object {
        const val MAX_ATTEMPTS = 5
        const val MULTIPLIER = 5.0
        private val INITIAL_INTERVAL = Duration.of(1L, ChronoUnit.SECONDS).toMillis()
        private val MAX_INTERVAL = Duration.of(5L, ChronoUnit.MINUTES).toMillis()
    }
}