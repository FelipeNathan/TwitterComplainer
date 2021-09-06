package br.com.twittercomplainer.configuration.scheduler

import br.com.twittercomplainer.persistence.PostRepository
import br.com.twittercomplainer.runnable.TwitterRunnableV1
import io.github.redouane59.twitter.TwitterClient
import java.util.TimeZone
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.config.TriggerTask
import org.springframework.scheduling.support.CronTrigger

@Configuration
class TwitterSchedulerV1(
    private val postRepository: PostRepository,
    private val twitterClient: TwitterClient
) : TwitterScheduler {

    override fun schedule(taskRegistrar: ScheduledTaskRegistrar) {

        postRepository.loadPostsV1()
            .also { LOGGER.info("Starting configuration of ${it.size} post schedulers V1") }
            .forEach { post ->
                val trigger = Trigger { triggerContext ->
                    CronTrigger(post.cron, ZONE_ID).nextExecutionTime(triggerContext)
                }

                post.run { TwitterRunnableV1(this, twitterClient) }
                    .run { TriggerTask(this, trigger) }
                    .run { taskRegistrar.scheduleTriggerTask(this) }
            }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterSchedulerV1::class.java)
        private val ZONE_ID = TimeZone.getTimeZone("America/Sao_Paulo").toZoneId()
    }
}