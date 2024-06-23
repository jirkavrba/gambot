package dev.vrba.discord.gambot.modules.roulette

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.integer
import dev.vrba.discord.gambot.discord.DiscordModule
import dev.vrba.discord.gambot.extensions.toUnit
import dev.vrba.discord.gambot.modules.balance.BalanceService
import dev.vrba.discord.gambot.modules.shared.UserId
import dev.vrba.discord.gambot.modules.shared.notEnoughBalanceEmbed
import org.springframework.stereotype.Component

@Component
class RouletteCommands(
    private val rouletteService: RouletteService,
    private val balanceService: BalanceService,
) : DiscordModule {
    override suspend fun register(client: Kord) {
        client.registerRouletteCommand()
        client.registerInteractionHandlers()
    }

    private suspend fun Kord.registerRouletteCommand() {
        on<ReadyEvent> {
            createGlobalChatInputCommand("roulette", "Place your bet and spin the roulette!") {
                integer("bid", "How much do you want to bid. Defaults to what you bid last time.") {
                    required = false
                }
            }
        }
    }

    private suspend fun Kord.registerInteractionHandlers() {
        on<ChatInputCommandInteractionCreateEvent> {
            if (interaction.invokedCommandName != "roulette") return@on

            val id = UserId(interaction.user)
            val bid = rouletteService.getLastRouletteBid(id) ?: 1.toBigInteger()
            val balance = balanceService.getUserBalance(id)

            if (balance < bid) {
                return@on interaction.respondEphemeral { notEnoughBalanceEmbed(balance) }.toUnit()
            }

            interaction.respondEphemeral {
                rootBetEmbed(bid)
                rootBetButtons()
            }
        }
    }
}
