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

    suspend fun saveAndSchedule(post: PostV1) {
        try {
            postCollection.save(post)
            postSchedulerV1.schedule(post)
        } catch (ex: Exception) {
            logger.error("Failed to save and/or schedule post ${post.id}")
            throw ex
        }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
