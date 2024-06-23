package dev.vrba.discord.gambot.modules.roulette

import dev.vrba.discord.gambot.modules.shared.UserId
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class RouletteService(
    template: RedisTemplate<String, BigInteger>,
) {
    private val redis = template.opsForValue()

    fun getLastRouletteBid(id: UserId): BigInteger? = redis.get(id.asRouletteBidKey())

    private fun UserId.asRouletteBidKey() = "roulette:last-bid:user:$value"
}
