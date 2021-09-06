package br.com.twittercomplainer.configuration

import io.github.redouane59.twitter.TwitterClient
import io.github.redouane59.twitter.signature.TwitterCredentials
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClientConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "twitter")
    fun getTwitterProperties() = TwitterProperties()

    @Bean
    fun getClient(twitterProperties: TwitterProperties) =
        TwitterClient(
            TwitterCredentials.builder()
                .accessToken(twitterProperties.token)
                .accessTokenSecret(twitterProperties.tokenSecret)
                .apiKey(twitterProperties.apiKey)
                .apiSecretKey(twitterProperties.apiSecretKey)
                .build()
        )
}
