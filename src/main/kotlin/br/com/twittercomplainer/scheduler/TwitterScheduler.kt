package br.com.twittercomplainer.scheduler

import br.com.twittercomplainer.configuration.ClientConfiguration
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TwitterScheduler {

    fun schedule() {

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            val daysWithoutAnswer = LocalDate.now().run {
                ChronoUnit.DAYS.between(LAST_RESPONSE_OF_UBER, this)
            }

            ClientConfiguration.getClient().postTweet(text(daysWithoutAnswer))
        }, INITIAL_DELAY, PERIOD, TimeUnit.DAYS)
    }

    private fun text(daysWithoutAnswer: Long) = """
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
    }
}