package br.com.twittercomplainer.runnable

import br.com.twittercomplainer.model.PostV1
import io.github.redouane59.twitter.TwitterClient
import io.github.redouane59.twitter.dto.tweet.Tweet
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TwitterRunnableV1Test : BehaviorSpec() {

    private val tweet: Tweet = mockk(relaxed = true)
    private val post: PostV1 = mockk(relaxed = true)
    private val twitterClient: TwitterClient = mockk(relaxed = true)
    private val runnable = TwitterRunnableV1(post, twitterClient)

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    init {

        Given("A post") {
            When("Too large post") {
                every { post.texts } returns listOf(largePost())

                runnable.run()

                Then("Should throw an IllegalArgumentException") {
                    verify(exactly = 0) { twitterClient.postTweet(any(), any()) }
                }
            }

            When("Just one text in array") {
                every { post.texts } returns listOf("Some post")

                runnable.run()

                Then("Should send one post") {
                    verify(exactly = 1) { twitterClient.postTweet("Some post", null) }
                }
            }

            When("Two texts in array and change variable") {

                every { post.lastAnswer } returns LocalDateTime.now().minusDays(2).format(DATE_FORMAT)
                every { post.texts } returns listOf("Some post", "{{daysWithoutAnswer}} days")
                every { tweet.id } returns "123"
                every { twitterClient.postTweet("Some post", null) } returns tweet

                runnable.run()

                Then("Should send two post") {
                    verify(exactly = 1) { twitterClient.postTweet("Some post", null) }
                    verify(exactly = 1) { twitterClient.postTweet("2 days", "123") }
                }
            }

            When("Two three texts in array should post three times with different ids") {

                every { post.lastAnswer } returns LocalDateTime.now().minusDays(2).format(DATE_FORMAT)
                every { post.texts } returns listOf("Some post", "{{daysWithoutAnswer}} days", "ha ha")
                every { tweet.id } returns "123" andThen "321"
                every { twitterClient.postTweet(any(), any()) } returns tweet

                runnable.run()

                Then("Should send three post") {
                    verify(exactly = 1) { twitterClient.postTweet("Some post", null) }
                    verify(exactly = 1) { twitterClient.postTweet("2 days", "123") }
                    verify(exactly = 1) { twitterClient.postTweet("ha ha", "321") }
                }
            }
        }
    }

    private fun largePost() = (1..100).joinToString { "large-text" }

    companion object {
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}
