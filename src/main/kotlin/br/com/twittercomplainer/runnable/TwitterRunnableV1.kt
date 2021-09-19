package br.com.twittercomplainer.runnable

import br.com.twittercomplainer.model.PostV1
import io.github.redouane59.twitter.TwitterClient
import org.slf4j.LoggerFactory
import java.security.InvalidParameterException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class TwitterRunnableV1(
    private val post: PostV1,
    private val twitterClient: TwitterClient
) : Runnable {

    init {
        logger.info("Creating one of the Runnable V1")
    }

    private val daysWithoutAnswer
        get() = post.lastAnswer
            ?.takeIf { it.isNotBlank() }
            ?.let {
                LocalDateTime.now().run {
                    val lastResponse =
                        DATE_FORMAT.parse(post.lastAnswer).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                    ChronoUnit.DAYS.between(lastResponse, this)
                }
            }

    override fun run() {
        try {
            var lastTweetId: String? = null

            post.texts.forEach { text ->
                text
                    .also(::validate)
                    .run(::replaceDaysWithoutAnswer)
                    .also(logger::info)
                    .also {
                        twitterClient.postTweet(it, lastTweetId).run {
                            lastTweetId = id
                            logger.info("Tweet posted, id: $lastTweetId")
                        }
                    }
            }
        } catch (ex: Exception) {
            logger.error("Failed to Post a Tweet: $ex")
        }
    }

    private fun validate(text: String) {
        if (text.length > 280) {
            throw InvalidParameterException(
                "Text size exceed the limit, break the text in more elements in the array"
            )
        }
    }

    private fun replaceDaysWithoutAnswer(text: String): String {
        return daysWithoutAnswer?.run {
            text.replace("{{daysWithoutAnswer}}", "$daysWithoutAnswer")
        } ?: text
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        private val logger = LoggerFactory.getLogger(TwitterRunnableV1::class.java)
    }
}
