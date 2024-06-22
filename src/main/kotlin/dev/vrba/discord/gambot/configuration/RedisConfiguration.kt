package dev.vrba.discord.gambot.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfiguration(
    private val factory: RedisConnectionFactory,
) {
    @Bean
    fun redisTemplateStringInt(): RedisTemplate<String, Long> =
        RedisTemplate<String, Long>().apply {
            connectionFactory = factory
        }
}
