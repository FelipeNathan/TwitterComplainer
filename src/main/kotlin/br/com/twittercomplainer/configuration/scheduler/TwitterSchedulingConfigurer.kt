package br.com.twittercomplainer.configuration.scheduler

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

@Configuration
class TwitterSchedulingConfigurer(
    private val schedulers: List<TwitterScheduler>
) : SchedulingConfigurer {

    private val taskScheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) = runBlocking {
        taskRegistrar.setScheduler(taskScheduler)
        schedulers.forEach { launch { it.schedule(taskRegistrar) } }
    }
}