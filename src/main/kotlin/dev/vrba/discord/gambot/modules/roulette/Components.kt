package dev.vrba.discord.gambot.modules.roulette

import dev.kord.common.entity.ButtonStyle
import dev.kord.rest.builder.message.MessageBuilder
import dev.kord.rest.builder.message.actionRow
import dev.kord.x.emoji.Emojis

fun MessageBuilder.rootBetButtons() {
    actionRow {
        interactionButton(ButtonStyle.Secondary, "roulette:numbers:selection") {
            label = "Numbers"
        }

        interactionButton(ButtonStyle.Secondary, "roulette:dozens") {
            label = "${Emojis.yellowSquare.unicode} Dozens"
        }

        interactionButton(ButtonStyle.Secondary, "roulette:columns") {
            label = "${Emojis.greenSquare.unicode} Columns"
        }
    }

    actionRow {
        interactionButton(ButtonStyle.Secondary, "roulette:low") {
            label = "Low numbers (1-18)"
        }

        interactionButton(ButtonStyle.Secondary, "roulette:high") {
            label = "High numbers (19-36)"
        }
    }

    actionRow {
        interactionButton(ButtonStyle.Secondary, "roulette:odd") {
            label = "Odd numbers"
        }

        interactionButton(ButtonStyle.Secondary, "roulette:even") {
            label = "Even numbers"
        }
    }

    actionRow {
        interactionButton(ButtonStyle.Danger, "roulette:red") {
            label = "Red"
        }

        interactionButton(ButtonStyle.Secondary, "roulette:black") {
            label = "Black"
        }
    }
}
