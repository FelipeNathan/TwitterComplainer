package br.com.twittercomplainer.model

import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bson.codecs.pojo.annotations.BsonRepresentation
import org.bson.types.ObjectId

data class PostV1(
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    var id: String? = null,
    var cron: String? = null,
    var lastAnswer: String? = null,
    var texts: List<String> = listOf()
) {

    @BsonIgnore
    fun getObjectId() = ObjectId(id)
}