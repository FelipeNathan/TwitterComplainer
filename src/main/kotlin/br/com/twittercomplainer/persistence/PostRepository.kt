package br.com.twittercomplainer.persistence

import br.com.twittercomplainer.model.TwitterFile
import br.com.twittercomplainer.model.PostV1
import com.google.gson.Gson
import java.io.File
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class PostRepository(
    @Value("\${persistence.file}") private var persistenceFile: String
) {

    val postsFile: TwitterFile = persistenceFile.ifBlank {
        this::class.java.getResource("/posts.json")?.file ?: ""
    }.run {
        val text = File(this).bufferedReader().readText()
        Gson().fromJson(text, TwitterFile::class.java)
    }

    fun loadPostsV1(): List<PostV1> {
        return postsFile.posts_v1 ?: listOf()
    }
}