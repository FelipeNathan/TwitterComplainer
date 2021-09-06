package br.com.twittercomplainer

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MainApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            LoggerFactory.getLogger(MainApplication::class.java).info("Starting APP")
            runApplication<MainApplication>()
        }
    }
}