package dev.vrba.discord.gambot.modules.roulette

import dev.kord.common.Color
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.effectiveName
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.integer
import dev.kord.rest.builder.message.embed
import dev.vrba.discord.gambot.discord.DiscordModule
import dev.vrba.discord.gambot.discord.effectiveAvatarUrl
import dev.vrba.discord.gambot.extensions.toUnit
import dev.vrba.discord.gambot.modules.balance.BalanceService
import dev.vrba.discord.gambot.modules.shared.Asset
import dev.vrba.discord.gambot.modules.shared.UserId
import dev.vrba.discord.gambot.modules.shared.notEnoughBalanceEmbed
import org.springframework.stereotype.Component
import java.math.BigInteger

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
            val bidSource = interaction.command.integers["bid"]?.toBigInteger()
            val bid =
                (bidSource ?: rouletteService.getLastRouletteBid(id, BigInteger.ONE)).also {
                    rouletteService.setRouletteBid(id, it)
                }

            val balance = balanceService.getUserBalance(id)

            if (balance < bid) {
                return@on interaction.respondEphemeral { notEnoughBalanceEmbed(balance) }.toUnit()
            }

            interaction.respondEphemeral {
                rootBetEmbed(bid)
                rootBetButtons()
            }
        }

        on<ButtonInteractionCreateEvent> {
            val id = interaction.componentId
            val user = UserId(interaction.user)
            val bid = rouletteService.getLastRouletteBid(user, 1.toBigInteger())

            when {
                id == "roulette:numbers" -> TODO()
                id == "roulette:dozens" -> TODO()
                id == "roulette:columns" -> TODO()
                id == "roulette:odd" -> TODO()
                id == "roulette:even" -> TODO()
                id == "roulette:low" -> TODO()
                id == "roulette:high" -> TODO()
                id == "roulette:black" -> handleBet(BlackNumberBet, user, bid, interaction)
                id == "roulette:red" -> handleBet(RedNumberBet, user, bid, interaction)
                id.startsWith("roulette:numbers:dozen-") -> TODO()
                id.startsWith("roulette:dozens:dozen-") -> TODO()
                id.startsWith("roulette:columns:column-") -> TODO()
            }
        }
    }

    private suspend fun handleBet(
        bet: Bet,
        user: UserId,
        amount: BigInteger,
        interaction: ButtonInteraction,
    ) {
        val deferred = interaction.deferPublicResponse()
        val result = rouletteService.performRouletteSpin(bet, user, amount)
        val numberImage =
            when (result.rolledNumber) {
                ZERO -> "number-zero.png"
                DOUBLE_ZERO -> "number-double-zero.png"
                else -> "number-${result.rolledNumber}.png"
            }

        val betImage =
            when (bet) {
                is ZeroBet -> "roulette-zero.png"
                is DoubleZeroBet -> "roulette-double-zero.png"
                is SingleNumberBet -> "roulette-single-${bet.number}.png"
                is DozenBet -> "roulette-dozen-${bet.base}.png"
                is ColumnBet -> "roulette-column-${bet.base}.png"
                is OddNumberBet -> "roulette-odd.png"
                is EvenNumberBet -> "roulette-event.png"
                is HighNumberBet -> "roulette-high.png"
                is LowNumberBet -> "roulette-low.png"
                is RedNumberBet -> "roulette-red.png"
                is BlackNumberBet -> "roulette-black.png"
            }

        deferred.respond {
            embed {
                author {
                    name = interaction.user.effectiveName
                    icon = interaction.user.effectiveAvatarUrl
                }

                color = Color(if (result.won) 0x57F287 else 0xED4245)
                title = "You put $amount coins on ${bet.name}"
                description = if (result.won) "And you won **${result.amountWon}** coins!" else "And you lost it all."
                image = Asset(betImage).url
                thumbnail {
                    url = Asset(numberImage).url
                }
            }
        }
    }
}
