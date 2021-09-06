package br.com.twittercomplainer.model

data class PostV1 (
    val cron: String,
    val lastAnswer: String?,
    val texts: List<String>
)