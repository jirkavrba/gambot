package dev.vrba.discord.gambot.modules.roulette

import dev.kord.common.entity.ButtonStyle
import dev.kord.rest.builder.message.MessageBuilder
import dev.kord.rest.builder.message.actionRow
import dev.kord.x.emoji.Emojis

fun MessageBuilder.rootBetButtons() {
    actionRow {
        interactionButton(ButtonStyle.Success, "roulette:numbers:0") {
            label = "0"
        }

        interactionButton(ButtonStyle.Success, "roulette:numbers:00") {
            label = "00"
        }

        interactionButton(ButtonStyle.Secondary, "roulette:numbers") {
            label = "1 - 36"
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

fun MessageBuilder.numberSelectionButtons() {
    actionRow {
        interactionButton(ButtonStyle.Primary, "roulette:numbers:dozen-1") {
            label = "1 - 12"
        }

        interactionButton(ButtonStyle.Danger, "roulette:numbers:dozen-13") {
            label = "13 - 24"
        }

        interactionButton(ButtonStyle.Success, "roulette:numbers:dozen-25") {
            label = "25 - 36"
        }
    }
}

fun MessageBuilder.dozenSelectionButtons() {
    actionRow {
        interactionButton(ButtonStyle.Primary, "roulette:dozens:dozen-1") {
            label = "1 - 12"
        }

        interactionButton(ButtonStyle.Danger, "roulette:dozens:dozen-13") {
            label = "13 - 24"
        }

        interactionButton(ButtonStyle.Success, "roulette:dozens:dozen-25") {
            label = "25 - 36"
        }
    }
}

fun MessageBuilder.columnSelectionButtons() {
    actionRow {
        interactionButton(ButtonStyle.Primary, "roulette:columns:column-1") {
            label = "1 - 34"
        }

        interactionButton(ButtonStyle.Danger, "roulette:columns:column-2") {
            label = "2 - 35"
        }

        interactionButton(ButtonStyle.Success, "roulette:columns:column-3") {
            label = "3 - 36"
        }
    }
}

fun MessageBuilder.numberSelectionDozenButtons(base: Int) {
    val numbers = base..(base + 11)
    val rows = numbers.chunked(3)

    rows.forEach { row ->
        actionRow {
            row.forEach { number ->
                interactionButton(
                    if (number in RED_NUMBERS) ButtonStyle.Danger else ButtonStyle.Secondary,
                    "roulette:numbers:number-$number",
                ) {
                    label = "$number"
                }
            }
        }
    }
}
