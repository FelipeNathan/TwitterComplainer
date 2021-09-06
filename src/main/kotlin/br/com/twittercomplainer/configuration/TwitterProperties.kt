package br.com.twittercomplainer.configuration

data class TwitterProperties(
    var token: String? = null,
    var tokenSecret: String? = null,
    var apiKey: String? = null,
    var apiSecretKey: String? = null
)
