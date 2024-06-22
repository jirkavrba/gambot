package dev.vrba.discord.gambot.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import java.math.BigInteger

@Configuration
class RedisConfiguration(
    private val factory: RedisConnectionFactory,
) {
    @Bean
    fun redisTemplateStringBigInteger() = RedisTemplate<String, BigInteger>().apply { connectionFactory = factory }

    @Bean
    fun redisTemplateStringBoolean() = RedisTemplate<String, Boolean>().apply { connectionFactory = factory }
}
