package br.com.twittercomplainer.model

data class TwitterPost (
    val cron: String,
    val lastAnswer: String,
    val texts: List<String>
)