package dev.vrba.discord.gambot.balance

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class BalanceService(
    template: RedisTemplate<String, Long>,
) {
    private val redis = template.opsForValue()

    private companion object {
        const val DEFAULT_BALANCE = 100L
    }

    fun getUserBalance(userId: String): Long = redis[userId.asUserBalanceKey()] ?: DEFAULT_BALANCE

    fun incrementUserBalanceBy(
        userId: String,
        amount: Long,
    ): Long =
        getUserBalance(userId)
            .plus(amount)
            .also { redis.set(userId.asUserBalanceKey(), it) }

    fun decrementUserBalanceBy(
        userId: String,
        amount: Long,
    ): Long =
        getUserBalance(userId)
            .minus(amount)
            .also { redis.set(userId.asUserBalanceKey(), it) }

    private fun String.asUserBalanceKey() = "balance:user:$this"
}
