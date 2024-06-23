package dev.vrba.discord.gambot.modules.shared

import dev.kord.common.Color
import dev.kord.rest.builder.message.MessageBuilder
import dev.kord.rest.builder.message.embed
import dev.kord.x.emoji.Emojis
import java.math.BigInteger

fun MessageBuilder.notEnoughBalanceEmbed(balance: BigInteger) {
    embed {
        color = Color(0xED4245)
        title = "You don't have enough coins!"
        description = "Your current balance is only $balance coins ${Emojis.moneybag.unicode}"
    }
}
