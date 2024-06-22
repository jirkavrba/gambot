package dev.vrba.discord.gambot.domain.balance

import dev.kord.common.Color
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.effectiveName
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.integer
import dev.kord.rest.builder.interaction.user
import dev.kord.rest.builder.message.embed
import dev.kord.x.emoji.Emojis
import dev.vrba.discord.gambot.discord.DiscordModule
import dev.vrba.discord.gambot.discord.effectiveAvatarUrl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BalanceCommands(
    private val service: BalanceService,
) : DiscordModule {
    private val logger = LoggerFactory.getLogger(this::class.qualifiedName)

    override suspend fun register(client: Kord) {
        client.registerBalanceCommands()
        client.registerCommandHandlers()
    }

    private suspend fun Kord.registerBalanceCommands() {
        on<ReadyEvent> {
            logger.info("Registering balance commands...")

            createGlobalChatInputCommand("balance", "Display the current balance")
            createGlobalChatInputCommand("transfer", "Transfer a specified amount to another user's account") {
                user("recipient", "The recipient that you want to transfer coins to.") {
                    required = true
                }

                integer("amount", "The number of coins you want to transfer. Cannot be negative.") {
                    required = true
                    minValue = 1
                }
            }
        }
    }

    private suspend fun Kord.registerCommandHandlers() {
        on<ChatInputCommandInteractionCreateEvent> {
            when (interaction.invokedCommandName) {
                "balance" -> handleBalanceCommand(interaction)
                "transfer" -> handleTransferCommand(interaction)
            }
        }
    }

    private suspend fun handleBalanceCommand(interaction: ChatInputCommandInteraction) {
        val deferred = interaction.deferPublicResponse()
        val balance = service.getUserBalance(interaction.user.id.toString())

        deferred.respond {
            embed {
                color = Color(0xFEE75C)
                title = "Your current balance is $balance coins ${Emojis.moneybag.unicode}"

                author {
                    icon = interaction.user.effectiveAvatarUrl
                    name = interaction.user.effectiveName
                }
            }
        }
    }

    private suspend fun handleTransferCommand(interaction: ChatInputCommandInteraction) {
        interaction.respondEphemeral {
            embed {
                color = Color(0xFEE75C)
                title = "This feature is not implemented yet."
            }
        }
    }
}
