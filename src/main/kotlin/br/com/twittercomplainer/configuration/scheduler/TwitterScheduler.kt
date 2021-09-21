package br.com.twittercomplainer.configuration.scheduler

interface TwitterScheduler {
    suspend fun schedule()
}
