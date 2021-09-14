package br.com.twittercomplainer.listener

import br.com.twittercomplainer.configuration.rabbitmq.PostsConfiguration.Companion.POSTS_QUEUE
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class PostListener(

) {

    @RabbitListener(queues = [POSTS_QUEUE])
    fun process(message: Message) {


        throw Exception("")
    }
}