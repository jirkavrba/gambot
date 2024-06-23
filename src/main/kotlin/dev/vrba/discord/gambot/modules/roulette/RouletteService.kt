package dev.vrba.discord.gambot.modules.roulette

import dev.vrba.discord.gambot.modules.balance.BalanceService
import dev.vrba.discord.gambot.modules.shared.UserId
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class RouletteService(
    template: RedisTemplate<String, BigInteger>,
    private val balanceService: BalanceService,
) {
    private val redis = template.opsForValue()

    data class RouletteSpinResult(
        val won: Boolean,
        val amountWon: BigInteger,
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

        balanceService.decrementUserBalanceBy(user, amount)

        if (won) {
            balanceService.incrementUserBalanceBy(user, amountWon)
        }

        return RouletteSpinResult(
            won = won,
            amountWon = amountWon,
            rolledNumber = number,
        )
    }

    fun getLastRouletteBid(
        id: UserId,
        default: BigInteger,
    ): BigInteger = redis.get(id.asRouletteBidKey()) ?: default

    fun setRouletteBid(
        id: UserId,
        bid: BigInteger,
    ) = redis.set(id.asRouletteBidKey(), bid)

    private fun UserId.asRouletteBidKey() = "roulette:last-bid:user:$value"
}
