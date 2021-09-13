package br.com.twittercomplainer.persistence

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId

abstract class TwitterDatabase<TCollection>(private val mongoSettings: MongoClientSettings) {

    suspend fun <R> withConnection(block: (database: MongoDatabase) -> R): R = withContext(IO) {
        MongoClients.create(mongoSettings).use {
            block(it.getDatabase("twitter"))
        }
    }

    suspend fun findAll(): MutableList<TCollection> = withConnection {
        it.getCollection().find().into(mutableListOf())
    }

    suspend fun findById(id: ObjectId): TCollection? = withConnection {
        it.getCollection().find(Filters.eq("_id", id)).first()
    }

    suspend fun delete(id: ObjectId) = withConnection {
        it.getCollection().deleteOne(Filters.eq("_id", id)).deletedCount
    }

    abstract fun MongoDatabase.getCollection(): MongoCollection<TCollection>
}