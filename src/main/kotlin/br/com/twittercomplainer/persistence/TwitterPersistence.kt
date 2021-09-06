package br.com.twittercomplainer.persistence

import br.com.twittercomplainer.model.TwitterPost
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class TwitterPersistence(
    @Value("\${persistence.file}") private var persistenceFile: String
) {

    fun loadPosts(): List<TwitterPost> {

        val filePath = if (persistenceFile.isNullOrBlank()) {
            this::class.java.getResource("/$FILE_NAME").file
        } else {
            persistenceFile
        }

        val text = File(filePath).bufferedReader().readText()

        return Gson().fromJson(text, Array<TwitterPost>::class.java).toList()
    }

    companion object {
        const val FILE_NAME = "posts.json"
    }
}