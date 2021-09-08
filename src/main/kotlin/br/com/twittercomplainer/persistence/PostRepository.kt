package br.com.twittercomplainer.persistence

import br.com.twittercomplainer.model.PostV1
import br.com.twittercomplainer.model.TwitterFile
import com.google.gson.Gson
import java.io.FileInputStream
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class PostRepository(
    @Value("\${persistence.file}") private var persistenceFile: String
) {

    val postsFile: TwitterFile =
        persistenceFile
            .let {
                if (it.isBlank()) {
                    this::class.java.getResourceAsStream("/posts.json")
                } else {
                    FileInputStream(it)
                }
            }
            .run { this.bufferedReader().readText()  }
            .run { Gson().fromJson(this, TwitterFile::class.java) }

    fun loadPostsV1(): List<PostV1> {
        return postsFile.posts_v1 ?: listOf()
    }
}