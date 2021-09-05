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

            try {
                var lastTweetId: String? = null
                buildTweets(daysWithoutAnswer).forEach { tweetText ->
                    LOGGER.info(tweetText)

                    ClientConfiguration.getClient().postTweet(tweetText, lastTweetId).run {
                        lastTweetId = id
                        LOGGER.info("Tweet posted, id: $lastTweetId")
                    }
                }
            } catch (ex: Exception) {
                LOGGER.error("Failed to Post a Tweet: $ex")
            }

        }, INITIAL_DELAY, PERIOD, TimeUnit.DAYS)
    }

    private fun buildTweets(daysWithoutAnswer: Long) = arrayOf(
        "[POST AUTOMÁTICO]\n\nHoje faz $daysWithoutAnswer dias que o @Uber_Support não resolve o meu problema e a resposta é sempre a mesma:\n" +
                " - Entendemos que esteja frustrado bla bla bla, revise o pedido bla bla bla, entre em contato bla bla bla",

        "O que não compreendem é que, infelizmente, nunca mais vou *conseguir* fazer pedido porque o app me impede!\n" +
                "Amanhã a contagem continua, até lá!"
    )

    companion object {
        const val INITIAL_DELAY = 0L
        const val PERIOD = 1L
        val LAST_RESPONSE_OF_UBER: LocalDate = LocalDate.of(2021, 8, 16)
        private val LOGGER = LoggerFactory.getLogger(TwitterScheduler::class.java)
    }
}