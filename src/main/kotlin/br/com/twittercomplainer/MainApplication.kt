package br.com.twittercomplainer

import br.com.twittercomplainer.scheduler.TwitterScheduler

class MainApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TwitterScheduler().schedule()
        }
    }
}