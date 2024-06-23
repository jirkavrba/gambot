package dev.vrba.discord.gambot.modules.roulette

const val ZERO = 0
const val DOUBLE_ZERO = 100

val BASE_NUMBERS = (1..36).toSet()
val ALL_NUMBERS = BASE_NUMBERS + ZERO + DOUBLE_ZERO
val LOW_NUMBERS = (1..18).toSet()
val HIGH_NUMBERS = (19..36).toSet()
val EVEN_NUMBERS = BASE_NUMBERS.filter { it % 2 == 0 }.toSet()
val ODD_NUMBERS = BASE_NUMBERS - EVEN_NUMBERS

val RED_NUMBERS = EVEN_NUMBERS
val BLACK_NUMBERS = ODD_NUMBERS
