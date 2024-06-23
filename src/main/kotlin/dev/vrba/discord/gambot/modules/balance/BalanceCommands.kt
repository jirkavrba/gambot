package dev.vrba.discord.gambot.modules.balance

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
import dev.vrba.discord.gambot.extensions.toUnit
import dev.vrba.discord.gambot.modules.shared.UserId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class BalanceCommands(
    private val balanceService: BalanceService,
    private val miningService: MiningService,
) : DiscordModule {
    private val logger = LoggerFactory.getLogger(this::class.qualifiedName)

    override suspend fun register(client: Kord) {
        client.registerBalanceCommands()
        client.registerCommandHandlers()
    }

    private suspend fun Kord.registerBalanceCommands() {
        on<ReadyEvent> {
            logger.info("Registering balance commands")

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

            createGlobalChatInputCommand("mine", "Try your luck and mine some sweet coins.")
        }
    }

    private suspend fun Kord.registerCommandHandlers() {
        on<ChatInputCommandInteractionCreateEvent> {
            when (interaction.invokedCommandName) {
                "balance" -> handleBalanceCommand(interaction)
                "transfer" -> handleTransferCommand(interaction)
                "mine" -> handleMineCommand(interaction)
            }
        }
    }

    private suspend fun handleBalanceCommand(interaction: ChatInputCommandInteraction) {
        val deferred = interaction.deferPublicResponse()
        val user = UserId(interaction.user)
        val balance = balanceService.getUserBalance(user)

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

    private suspend fun handleMineCommand(interaction: ChatInputCommandInteraction) {
        val deferred = interaction.deferPublicResponse()
        val user = UserId(interaction.user)

        if (miningService.isUserOnCooldown(user)) {
            return deferred
                .respond {
                    embed {
                        color = Color(0xED4245)
                        title = "You're on cooldown! Try again later"
                        description = "The cooldown for mining coins is ${miningService.cooldownSeconds()} seconds."

                        author {
                            icon = interaction.user.effectiveAvatarUrl
                            name = interaction.user.effectiveName
                        }
                    }
                }.toUnit()
        }

        val result = miningService.mine(user)

        deferred.respond {
            embed {
                color = Color(0x57F287)
                title = "You mined ${result.minedCoins} coins!"
                description = "Your current coin balance is **${result.currentBalance} coins** ${Emojis.moneybag.unicode}."

                author {
                    icon = interaction.user.effectiveAvatarUrl
                    name = interaction.user.effectiveName
                }
            }
        }
    }
}
