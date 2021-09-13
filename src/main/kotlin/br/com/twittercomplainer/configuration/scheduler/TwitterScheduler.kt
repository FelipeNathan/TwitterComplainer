package br.com.twittercomplainer.configuration.scheduler

import org.springframework.scheduling.config.ScheduledTaskRegistrar

interface TwitterScheduler {
    suspend fun schedule(taskRegistrar: ScheduledTaskRegistrar)
}
