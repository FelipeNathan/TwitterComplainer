package br.com.twittercomplainer.configuration

import io.github.redouane59.twitter.TwitterClient
import io.github.redouane59.twitter.signature.TwitterCredentials

object ClientConfiguration {

    fun getClient() =
        TwitterClient(
            TwitterCredentials.builder()
                .accessToken(System.getenv("TWITTER_TOKEN"))
                .accessTokenSecret(System.getenv("TWITTER_TOKEN_SECRET"))
                .apiKey(System.getenv("TWITTER_API_KEY"))
                .apiSecretKey(System.getenv("TWITTER_API_SECRET_KEY"))
                .build()
        )
}