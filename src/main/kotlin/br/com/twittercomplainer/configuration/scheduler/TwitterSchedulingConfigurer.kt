package br.com.twittercomplainer.configuration.scheduler

import javax.annotation.PostConstruct
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TwitterSchedulingConfigurer(
    private val schedulers: List<TwitterScheduler>
) {

    @PostConstruct
    fun initSchedulers() = runBlocking { schedulers.forEach { launch { it.schedule() } } }
}