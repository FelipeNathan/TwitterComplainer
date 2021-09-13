package br.com.twittercomplainer.configuration.scheduler

import br.com.twittercomplainer.model.PostV1
import br.com.twittercomplainer.persistence.PostCollection
import io.github.redouane59.twitter.TwitterClient
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.springframework.scheduling.config.ScheduledTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar

class PostSchedulerV1Test : BehaviorSpec() {

    private val postCollection: PostCollection = mockk(relaxed = true)
    private val twitterClient: TwitterClient = mockk()
    private val taskRegistrar: ScheduledTaskRegistrar = mockk()
    private val scheduledTask: ScheduledTask = mockk()

    private val scheduler = PostSchedulerV1(postCollection, twitterClient)

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    init {

        val posts = mutableListOf(createV1Post())

        Given("Loaded posts") {
            When("Have posts") {

                coEvery { postCollection.findAll() } returns posts
                coEvery { taskRegistrar.scheduleTriggerTask(any()) } returns scheduledTask

                scheduler.schedule(taskRegistrar)

                Then("Should log Starting configuration of 1 post schedulers V1") {
                    coVerify(exactly = 1) { taskRegistrar.scheduleTriggerTask(any()) }
                }
            }

            When("Have no posts") {
                coEvery { postCollection.findAll() } returns mutableListOf()

                scheduler.schedule(taskRegistrar)

                Then("Should log Starting configuration of 1 post schedulers V1") {
                    coVerify(exactly = 0) { taskRegistrar.scheduleTriggerTask(any()) }
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