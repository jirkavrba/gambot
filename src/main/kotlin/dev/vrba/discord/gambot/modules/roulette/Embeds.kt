package dev.vrba.discord.gambot.modules.roulette

import dev.kord.rest.builder.message.MessageBuilder
import dev.kord.rest.builder.message.embed
import dev.vrba.discord.gambot.modules.shared.Asset
import java.math.BigInteger

fun MessageBuilder.rootBetEmbed(bid: BigInteger) {
    embed {
        title = "Select what you want to bet on."
        description = "Your bid is $bid coins"
        image = Asset("roulette.png").url
    }
}
