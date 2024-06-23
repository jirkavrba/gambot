package dev.vrba.discord.gambot.modules.balance

import dev.vrba.discord.gambot.modules.shared.UserId
import io.lettuce.core.api.StatefulRedisConnection
import jakarta.inject.Singleton
import java.math.BigInteger

@Singleton
class BalanceService(
    connection: StatefulRedisConnection<String, String>,
) {
    private val redis = connection.sync()

    private companion object {
        val DEFAULT_BALANCE: BigInteger = BigInteger.valueOf(100)
    }

    fun getUserBalance(id: UserId): BigInteger = redis[id.asUserBalanceKey()]?.toBigInteger() ?: DEFAULT_BALANCE

    fun incrementUserBalanceBy(
        id: UserId,
        amount: BigInteger,
    ): BigInteger =
        getUserBalance(id)
            .plus(amount)
            .also { redis.set(id.asUserBalanceKey(), it.toString()) }

    fun decrementUserBalanceBy(
        id: UserId,
        amount: BigInteger,
    ): BigInteger =
        getUserBalance(id)
            .minus(amount)
            .also { redis.set(id.asUserBalanceKey(), it.toString()) }

    fun transferBalance(
        sender: UserId,
        recipient: UserId,
        amount: BigInteger,
    ) {
        redis.set(sender.asUserBalanceKey(), (getUserBalance(sender) - amount).toString())
        redis.set(recipient.asUserBalanceKey(), (getUserBalance(recipient) + amount).toString())
    }

    private fun UserId.asUserBalanceKey() = "balance:user:$value"
}
