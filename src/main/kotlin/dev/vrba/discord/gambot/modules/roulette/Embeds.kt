package dev.vrba.discord.gambot.modules.roulette

import dev.kord.rest.builder.message.MessageBuilder
import dev.kord.rest.builder.message.embed
import dev.vrba.discord.gambot.modules.shared.Asset
import java.math.BigInteger

fun MessageBuilder.rootBetEmbed(bid: BigInteger) {
    embed {
        title = "Select what you want to bet on."
        description = "You're betting **$bid** coins."
        image = Asset("roulette.png").url
        footer {
            text = "To cancel the bet, simply dismiss this message."
        }
    }
}

fun MessageBuilder.numberSelectionEmbed(bid: BigInteger) {
    embed {
        title = "Select the dozen that contains your selected number"
        description = "You're betting **$bid** coins."
        image = Asset("roulette-dozens.png").url
        footer {
            text = "To cancel the bet, simply dismiss this message."
        }
    }
}

fun MessageBuilder.numberSelectionDozenEmbed(
    bid: BigInteger,
    dozenBase: Int,
) {
    embed {
        title = "Click on your selected number"
        description = "You're betting **$bid** coins."
        image = Asset("roulette-dozen-$dozenBase.png").url
        footer {
            text = "To cancel the bet, simply dismiss this message."
        }
    }
}

fun MessageBuilder.dozenSelectionEmbed(bid: BigInteger) {
    embed {
        title = "Select the dozen that you want to put your money on"
        description = "You're betting **$bid** coins."
        image = Asset("roulette-dozens.png").url
        footer {
            text = "To cancel the bet, simply dismiss this message."
        }
    }
}

fun MessageBuilder.columnSelectionEmbed(bid: BigInteger) {
    embed {
        title = "Select the column that you want to put your money on"
        description = "You're betting **$bid** coins."
        image = Asset("roulette-columns.png").url
        footer {
            text = "To cancel the bet, simply dismiss this message."
        }
    }
}
