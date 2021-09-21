package br.com.twittercomplainer.service

import br.com.twittercomplainer.configuration.scheduler.PostSchedulerV1
import br.com.twittercomplainer.model.PostV1
import br.com.twittercomplainer.persistence.PostCollection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postCollection: PostCollection,
    private val postSchedulerV1: PostSchedulerV1
) {

    suspend fun process(post: PostV1) {

        try {
            postCollection.save(post)

            if (post.enabled) {
                postSchedulerV1.schedule(post)
            } else {
                postSchedulerV1.cancelScheduledTask(post.id!!)
            }
        } catch (ex: Exception) {
            logger.error("Failed to save and/or schedule post ${post.id}")
            throw ex
        }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
