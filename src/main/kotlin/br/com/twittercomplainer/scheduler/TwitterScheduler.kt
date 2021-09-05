package br.com.twittercomplainer.scheduler

import br.com.twittercomplainer.configuration.ClientConfiguration
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

class TwitterScheduler {

    fun schedule() {

        LOGGER.info("Starting schedule")
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            val daysWithoutAnswer = LocalDate.now().run {
                ChronoUnit.DAYS.between(LAST_RESPONSE_OF_UBER, this)
            }

            val text = buildText(daysWithoutAnswer)
            LOGGER.info(text)

            try {
                val response = ClientConfiguration.getClient().postTweet(text)
                LOGGER.info("Tweet posted, id: ${response.id}")
            } catch (ex: Exception) {
                LOGGER.error("Failed to Post a Tweet: $ex")
            }

        }, INITIAL_DELAY, PERIOD, TimeUnit.DAYS)
    }

    private fun buildText(daysWithoutAnswer: Long) = """
        Este é um post automático,
        
        Hoje faz $daysWithoutAnswer dias que o @Uber_Support não resolve o meu problema e a resposta é sempre a mesma:
        - Entendemos que esteja frustrado bla bla bla, revise o pedido bla bla bla, entre em contato bla bla bla
        
        Infelizmente eu nunca mais vou *conseguir* fazer pedido porque o app me impede!
        
        Amanhã a contagem continua, até lá!
    """.trimIndent()

    companion object {
        const val INITIAL_DELAY = 0L
        const val PERIOD = 1L
        val LAST_RESPONSE_OF_UBER: LocalDate = LocalDate.of(2021, 8, 16)
        private val LOGGER = LoggerFactory.getLogger(TwitterScheduler::class.java)
    }
}