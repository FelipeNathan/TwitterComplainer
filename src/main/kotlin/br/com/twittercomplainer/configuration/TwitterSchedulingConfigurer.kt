package br.com.twittercomplainer.configuration

import br.com.twittercomplainer.persistence.TwitterPersistence
import br.com.twittercomplainer.runnable.TwitterRunnable
import io.github.redouane59.twitter.TwitterClient
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.support.CronTrigger
import java.util.*

@Configuration
@EnableScheduling
class TwitterSchedulingConfigurer(
    private val twitterPersistence: TwitterPersistence,
    private val twitterClient: TwitterClient
) : SchedulingConfigurer {

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {

        twitterPersistence.loadPosts().forEach {
            val trigger = Trigger { triggerContext ->
                CronTrigger(it.cron, zoneId).nextExecutionTime(triggerContext)
            }

            taskRegistrar.addTriggerTask(TwitterRunnable(it, twitterClient), trigger)
        }
    }


    companion object {
        private val zoneId = TimeZone.getTimeZone("America/Sao_Paulo").toZoneId()
    }
}