package br.com.twittercomplainer.runnable

import br.com.twittercomplainer.model.TwitterPost
import io.github.redouane59.twitter.TwitterClient
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class TwitterRunnable(
    private val post: TwitterPost,
    private val twitterClient: TwitterClient
) : Runnable {

    override fun run() {
        LOGGER.info("Starting schedule")

        val daysWithoutAnswer = LocalDateTime.now().run {
            val lastResponse =
                DATE_FORMAT.parse(post.lastAnswer).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            ChronoUnit.DAYS.between(lastResponse, this)
        }

        try {

            var lastTweetId: String? = null
            post.texts.forEach { tweetText ->
                val finalText = tweetText.replace("{{daysWithoutAnswer}}", "$daysWithoutAnswer")
                LOGGER.info(finalText)

                twitterClient.postTweet(finalText, lastTweetId).run {
                    lastTweetId = id
                    LOGGER.info("Tweet posted, id: $lastTweetId")
                }
            }
        } catch (ex: Exception) {
            LOGGER.error("Failed to Post a Tweet: $ex")
        }
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        private val LOGGER = LoggerFactory.getLogger(TwitterRunnable::class.java)
    }
}
