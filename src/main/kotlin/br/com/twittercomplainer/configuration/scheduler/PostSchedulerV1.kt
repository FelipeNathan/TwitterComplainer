package br.com.twittercomplainer.configuration.scheduler

import br.com.twittercomplainer.model.PostV1
import br.com.twittercomplainer.persistence.PostCollection
import br.com.twittercomplainer.runnable.TwitterRunnableV1
import io.github.redouane59.twitter.TwitterClient
import java.util.TimeZone
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import org.slf4j.LoggerFactory
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component

@Component
class PostSchedulerV1(
    private val postCollection: PostCollection,
    private val twitterClient: TwitterClient
) : TwitterScheduler {

    private val taskScheduler = ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor())
    private val scheduledMap = mutableMapOf<String, ScheduledFuture<*>>()

    override suspend fun schedule() {

        postCollection.findAllEnabled()
            .also { logger.info("Starting configuration of ${it.size} post schedulers V1") }
            .forEach { post -> schedule(post) }
    }

    suspend fun schedule(post: PostV1) {
        try {
            logger.info("Scheduling post ${post.id}")

            cancelScheduledTask(post.id!!)

            val trigger = Trigger { triggerContext ->
                CronTrigger(post.cron!!, ZONE_ID).nextExecutionTime(triggerContext)
            }

            post.run { TwitterRunnableV1(this, twitterClient) }
                .run { taskScheduler.schedule(this, trigger) }
                .run { scheduledMap.put(post.id!!, this) }

        } catch (ex: Exception) {
            logger.error("Failed to schedule post ${post.id}", ex)
        }
    }

    suspend fun cancelScheduledTask(id: String) = scheduledMap[id]?.cancel(true)

    companion object {
        private val logger = LoggerFactory.getLogger(PostSchedulerV1::class.java)
        private val ZONE_ID = TimeZone.getTimeZone("America/Sao_Paulo").toZoneId()
    }
}