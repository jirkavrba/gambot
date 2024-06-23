package dev.vrba.discord.gambot.modules.shared

import dev.kord.core.entity.User

@JvmInline
value class UserId(
    val value: String,
) {
    constructor(user: User) : this(user.id.toString())
}
