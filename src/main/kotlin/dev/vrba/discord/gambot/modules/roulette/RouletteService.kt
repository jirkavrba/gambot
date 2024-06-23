package dev.vrba.discord.gambot.modules.roulette

import dev.vrba.discord.gambot.modules.balance.BalanceService
import dev.vrba.discord.gambot.modules.shared.UserId
import io.lettuce.core.api.StatefulRedisConnection
import jakarta.inject.Singleton
import java.math.BigInteger

@Singleton
class RouletteService(
    template: StatefulRedisConnection<String, String>,
    private val balanceService: BalanceService,
) {
    private val redis = template.sync()

    data class RouletteSpinResult(
        val won: Boolean,
        val amountWon: BigInteger,
        val remainingBalance: BigInteger,
        val rolledNumber: Int,
    )

    fun performRouletteSpin(
        bet: Bet,
        user: UserId,
        amount: BigInteger,
    ): RouletteSpinResult {
        val number = ALL_NUMBERS.random()
        val won = number in bet.matchingNumbers
        val amountWon = if (won) amount * bet.payoutMultiplier.toBigInteger() else BigInteger.ZERO
        var remainingBalance = balanceService.decrementUserBalanceBy(user, amount)

        if (won) {
            remainingBalance = balanceService.incrementUserBalanceBy(user, amountWon)
        }

        return RouletteSpinResult(
            won = won,
            amountWon = amountWon,
            remainingBalance = remainingBalance,
            rolledNumber = number,
        )
    }

    fun getLastRouletteBid(
        id: UserId,
        default: BigInteger,
    ): BigInteger = redis.get(id.asRouletteBidKey())?.toBigInteger() ?: default

    fun setRouletteBid(
        id: UserId,
        bid: BigInteger,
    ) = redis.set(id.asRouletteBidKey(), bid.toString())

    private fun UserId.asRouletteBidKey() = "roulette:last-bid:user:$value"
}
