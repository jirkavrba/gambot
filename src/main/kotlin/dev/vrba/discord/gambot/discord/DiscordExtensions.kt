package dev.vrba.discord.gambot.discord

import dev.kord.core.entity.User

val User.effectiveAvatarUrl: String
    get() = (avatar ?: defaultAvatar).cdnUrl.toUrl()
