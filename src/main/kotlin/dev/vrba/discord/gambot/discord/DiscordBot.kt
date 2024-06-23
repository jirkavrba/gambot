package dev.vrba.discord.gambot.discord

import dev.kord.core.Kord
import io.micronaut.context.annotation.Value
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

@Singleton
class DiscordBot(
    @Value("\${gambot.discord.token}")
    private val token: String,
    private val modules: List<DiscordModule>,
) : ApplicationEventListener<StartupEvent> {
    private val logger = LoggerFactory.getLogger(this::class.qualifiedName)

    override fun onApplicationEvent(event: StartupEvent?) {
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
