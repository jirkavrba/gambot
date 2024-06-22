package dev.vrba.discord.gambot.discord

import dev.kord.core.Kord

fun interface DiscordModule {
    suspend fun register(client: Kord)
}
