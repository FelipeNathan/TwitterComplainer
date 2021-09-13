package br.com.twittercomplainer.persistence

import br.com.twittercomplainer.model.PostV1
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
class PostCollection(mongoSettings: MongoClientSettings) : TwitterDatabase<PostV1>(mongoSettings) {

    suspend fun save(post: PostV1) = withConnection {
        with(it.getCollection()) {
            when {
                post.id == null -> insertOne(post)
                else -> replaceOne(Filters.eq("_id", post.id), post)
            }
        }
    }

    suspend fun delete(post: PostV1) = post.id?.run { delete(this) }

    override fun MongoDatabase.getCollection() = getCollection("posts", PostV1::class.java)
}