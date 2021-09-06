package br.com.twittercomplainer.configuration.scheduler

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
class TwitterSchedulingConfigurer(
    private val schedulers: List<TwitterScheduler>
) : SchedulingConfigurer {

    private val taskScheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler)
        schedulers.forEach{ it.schedule(taskRegistrar) }
    }
}