package br.com.twittercomplainer

import br.com.twittercomplainer.scheduler.TwitterScheduler
import org.slf4j.LoggerFactory

class MainApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            LoggerFactory.getLogger(MainApplication::class.java).info("Starting APP")
            TwitterScheduler().schedule()
        }
    }
}