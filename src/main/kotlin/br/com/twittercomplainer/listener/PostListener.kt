package br.com.twittercomplainer.listener

import br.com.twittercomplainer.configuration.rabbitmq.PostsConfiguration.Companion.QUEUE
import br.com.twittercomplainer.model.PostV1
import br.com.twittercomplainer.service.PostService
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class PostListener(
    private val postService: PostService
) {

    @RabbitListener(queues = [QUEUE])
    fun process(message: Message) = runBlocking {
        postService.saveAndSchedule(message.post())
    }

    fun Message.post(): PostV1 = Gson().fromJson(String(this.body), PostV1::class.java)
}