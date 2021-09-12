package br.com.twittercomplainer.model

import org.bson.types.ObjectId

data class PostV1(
    var id: ObjectId? = null,
    var cron: String? = null,
    var lastAnswer: String? = null,
    var texts: List<String> = listOf()
)