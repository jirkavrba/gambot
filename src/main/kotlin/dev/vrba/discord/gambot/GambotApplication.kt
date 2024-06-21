package dev.vrba.discord.gambot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GambotApplication

fun main(args: Array<String>) {
	runApplication<GambotApplication>(*args)
}
