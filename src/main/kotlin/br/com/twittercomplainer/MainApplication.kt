package br.com.twittercomplainer

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MainApplication : CommandLineRunner {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<MainApplication>()
        }
    }

    override fun run(vararg args: String?) {
        LoggerFactory.getLogger(MainApplication::class.java).info("Starting APP")
        Thread.currentThread().join()
    }
}