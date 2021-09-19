package br.com.twittercomplainer.persistence

import br.com.twittercomplainer.model.PostV1
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.springframework.stereotype.Component

@Component
class PostCollection(mongoSettings: MongoClientSettings) : TwitterDatabase<PostV1>(mongoSettings) {

    suspend fun save(post: PostV1) = withConnection {
        with(it.getCollection()) {
            when {
                post.id.isNullOrEmpty() -> insertOne(post)
                else -> replaceOne(Filters.eq("_id", post.getObjectId()), post)
            }
        }
    }

    suspend fun delete(post: PostV1) = post
        .takeIf { it.id != null }
        ?.let { it.getObjectId() }
        ?.let { super.delete(it) }

    override fun MongoDatabase.getCollection() = getCollection("posts", PostV1::class.java)
}