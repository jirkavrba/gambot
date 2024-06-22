package dev.vrba.discord.gambot.domain.balance

import dev.vrba.discord.gambot.domain.UserId
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class BalanceService(
    template: RedisTemplate<String, BigInteger>,
) {
    private val redis = template.opsForValue()

    private companion object {
        val DEFAULT_BALANCE: BigInteger = BigInteger.valueOf(100)
    }

    fun getUserBalance(id: UserId): BigInteger = redis[id.asUserBalanceKey()] ?: DEFAULT_BALANCE

    fun incrementUserBalanceBy(
        id: UserId,
        amount: BigInteger,
    ): BigInteger =
        getUserBalance(id)
            .plus(amount)
            .also { redis.set(id.asUserBalanceKey(), it) }

    fun decrementUserBalanceBy(
        id: UserId,
        amount: BigInteger,
    ): BigInteger =
        getUserBalance(id)
            .minus(amount)
            .also { redis.set(id.asUserBalanceKey(), it) }

    fun transferBalance(
        sender: UserId,
        recipient: UserId,
        amount: BigInteger,
    ) {
        redis.set(sender.asUserBalanceKey(), getUserBalance(sender) - amount)
        redis.set(recipient.asUserBalanceKey(), getUserBalance(recipient) + amount)
    }

    private fun UserId.asUserBalanceKey() = "balance:user:$value"
}
