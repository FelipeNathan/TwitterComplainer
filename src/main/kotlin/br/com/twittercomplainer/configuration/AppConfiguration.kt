package br.com.twittercomplainer.configuration

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@EnableRabbit
class AppConfiguration
