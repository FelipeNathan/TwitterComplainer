package br.com.twittercomplainer.configuration.scheduler

import br.com.twittercomplainer.persistence.PostCollection
import br.com.twittercomplainer.runnable.TwitterRunnableV1
import io.github.redouane59.twitter.TwitterClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.config.TriggerTask
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component
import java.util.*

@Component
class PostSchedulerV1(
    private val postCollection: PostCollection,
    private val twitterClient: TwitterClient
) : TwitterScheduler {

    override suspend fun schedule(taskRegistrar: ScheduledTaskRegistrar) {

        postCollection.findAll()
            .also { LOGGER.info("Starting configuration of ${it.size} post schedulers V1") }
            .forEach { post ->
                try {
                    val trigger = Trigger { triggerContext ->
                        CronTrigger(post.cron!!, ZONE_ID).nextExecutionTime(triggerContext)
                    }

                    post.run { TwitterRunnableV1(this, twitterClient) }
                        .run { TriggerTask(this, trigger) }
                        .run { taskRegistrar.scheduleTriggerTask(this) }

                } catch (ex: Exception) {
                    LOGGER.error("Failed to schedule post ${post.id}", ex)
                }
            }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(PostSchedulerV1::class.java)
        private val ZONE_ID = TimeZone.getTimeZone("America/Sao_Paulo").toZoneId()
    }
}