package dev.vrba.discord.gambot.domain.balance

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

    fun transferBalance(
        fromUserId: String,
        toUserId: String,
        amount: Long,
    ) {
        redis.set(fromUserId.asUserBalanceKey(), getUserBalance(fromUserId) - amount)
        redis.set(toUserId.asUserBalanceKey(), getUserBalance(toUserId) + amount)
    }

    private fun String.asUserBalanceKey() = "balance:user:$this"
}
