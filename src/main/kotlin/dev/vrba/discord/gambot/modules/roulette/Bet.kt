package dev.vrba.discord.gambot.modules.roulette

sealed interface Bet {
    val name: String
    val matchingNumbers: Set<Int>
    val payoutMultiplier: Int
}

data class SingleNumberBet(
    val number: Int,
) : Bet {
    override val name: String = "a single number: $number"
    override val matchingNumbers: Set<Int> = setOf(number)
    override val payoutMultiplier: Int = 35
}

data object ZeroBet : Bet {
    override val name = "zero"
    override val matchingNumbers = setOf(ZERO)
    override val payoutMultiplier = 35
}

data object DoubleZeroBet : Bet {
    override val name = "double zero"
    override val matchingNumbers = setOf(DOUBLE_ZERO)
    override val payoutMultiplier = 35
}

data class DozenBet(
    val base: Int,
) : Bet {
    override val name = "a dozen $base - ${base + 11}"
    override val matchingNumbers = (base..(base + 11)).toSet()
    override val payoutMultiplier = 3
}

data class ColumnBet(
    val base: Int,
) : Bet {
    override val name = "a column $base - ${base + 33}"
    override val matchingNumbers = BASE_NUMBERS.filter { (it % 3) == (base % 3) }.toSet()
    override val payoutMultiplier: Int = 3
}

data object LowNumberBet : Bet {
    override val name = "low numbers"
    override val matchingNumbers = LOW_NUMBERS
    override val payoutMultiplier = 2
}

data object HighNumberBet : Bet {
    override val name = "high numbers"
    override val matchingNumbers = HIGH_NUMBERS
    override val payoutMultiplier = 2
}

data object EvenNumberBet : Bet {
    override val name = "even numbers"
    override val matchingNumbers = EVEN_NUMBERS
    override val payoutMultiplier = 2
}

data object OddNumberBet : Bet {
    override val name = "odd numbers"
    override val matchingNumbers = ODD_NUMBERS
    override val payoutMultiplier = 2
}

data object RedNumberBet : Bet {
    override val name = "red"
    override val matchingNumbers = RED_NUMBERS
    override val payoutMultiplier = 2
}

data object BlackNumberBet : Bet {
    override val name = "black"
    override val matchingNumbers = BLACK_NUMBERS
    override val payoutMultiplier = 2
}
