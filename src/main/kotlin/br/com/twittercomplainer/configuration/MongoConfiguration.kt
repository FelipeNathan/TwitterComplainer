package br.com.twittercomplainer.configuration

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoConfiguration(@Value("\${mongo.db.url}") private val mongoDbUrl: String) {

    @Bean
    fun mongoClientSettings() = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(mongoDbUrl))
        .codecRegistry(
            fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
            )
        ).build()
}