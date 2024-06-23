package dev.vrba.discord.gambot.modules.balance

import dev.vrba.discord.gambot.modules.shared.UserId
import io.lettuce.core.api.StatefulRedisConnection
import jakarta.inject.Singleton
import java.math.BigInteger

@Singleton
class MiningService(
    connection: StatefulRedisConnection<String, String>,
    private val balanceService: BalanceService,
) {
    data class MiningResult(
        val minedCoins: BigInteger,
        val currentBalance: BigInteger,
    )

    private val redis = connection.sync()

    private companion object {
        const val USER_COOLDOWN_SECONDS = 60L
        const val MONEY_LOWER_BOUND = 1
        const val MONEY_UPPER_BOUND = 50
    }

    fun cooldownSeconds(): Long = USER_COOLDOWN_SECONDS

    fun isUserOnCooldown(id: UserId): Boolean = redis.get(id.asMiningCooldownKey()) == "true"

    fun mine(id: UserId): MiningResult {
        if (isUserOnCooldown(id)) {
            throw IllegalStateException("Mining while still on cooldown, this should not happen!")
        }

        val amount = (MONEY_LOWER_BOUND..MONEY_UPPER_BOUND).random().toBigInteger()
        val balance = balanceService.incrementUserBalanceBy(id, amount)

        redis.setex(id.asMiningCooldownKey(), USER_COOLDOWN_SECONDS, "true")

        return MiningResult(
            minedCoins = amount,
            currentBalance = balance,
        )
    }

    private fun UserId.asMiningCooldownKey() = "mining:cooldown:user:$value"
}
