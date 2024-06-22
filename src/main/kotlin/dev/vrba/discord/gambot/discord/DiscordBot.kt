package dev.vrba.discord.gambot.discord

import dev.kord.core.Kord
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service

@Service
class DiscordBot(
    @Value("\${gambot.discord.token}")
    private val token: String,
    private val modules: List<DiscordModule>,
) : ApplicationRunner {
    private val logger = LoggerFactory.getLogger(this::class.qualifiedName)

    override fun run(args: ApplicationArguments?) {
        runBlocking {
            logger.info("Starting the gambot discord bot.")

            val client = Kord(token)

            modules.forEach {
                it.register(client)
            }

            client.login()
        }
    }
}
