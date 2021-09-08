package br.com.twittercomplainer.persistence

import br.com.twittercomplainer.model.PostV1
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class PostRepositoryTest : BehaviorSpec() {

    init {

        Given("V1 file") {
            val v1File = PostRepositoryTest::class.java.getResource("/posts_v1.json").file
            val repository = PostRepository(v1File)

            When("Search for V1 posts") {
                Then("Should return one post") {
                    val posts = repository.loadPostsV1()
                    val expected = createV1Post()

                    posts shouldHaveSize 1
                    posts[0].cron shouldBe expected.cron
                    posts[0].lastAnswer shouldBe expected.lastAnswer
                    posts[0].texts[0] shouldBe expected.texts[0]
                    posts[0].texts[1] shouldBe expected.texts[1]
                }
            }
        }

        Given("V2 file") {
            val v1File = PostRepositoryTest::class.java.getResource("/posts_v2.json").file
            val repository = PostRepository(v1File)

            When("Search for V1 posts") {
                Then("Should return one post") {
                    val posts = repository.loadPostsV1()
                    posts shouldHaveSize 0
                }
            }
        }

    }

    private fun createV1Post() = PostV1(
        cron = "0 0 12 * * ?",
        lastAnswer = "2021-08-16 00:00:00",
        texts = listOf(
            "[POST AUTOMÁTICO]\n\nHoje faz {{daysWithoutAnswer}} dias que o Uber_Support não resolve o meu problema e a resposta é sempre a mesma:\n - Entendemos que esteja frustrado bla bla bla, revise o pedido bla bla bla, entre em contato bla bla bla\n",
            "O que não compreendem é que, infelizmente, nunca mais vou *conseguir* fazer pedido porque o app me impede!\\n\nAmanhã a contagem continua, até lá!"
        )
    )
}