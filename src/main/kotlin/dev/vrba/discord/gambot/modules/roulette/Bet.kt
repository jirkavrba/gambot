package dev.vrba.discord.gambot.modules.roulette

sealed interface Bet {
    val name: String
    val matchingNumbers: Set<Int>
    val payoutMultiplier: Int
}

class SingleNumberBet(
    number: Int,
) : Bet {
    override val name: String = "Single number: $number"
    override val matchingNumbers: Set<Int> = setOf(number)
    override val payoutMultiplier: Int = 35
}

data object ZeroBet : Bet {
    override val name = "0"
    override val matchingNumbers = setOf(ZERO)
    override val payoutMultiplier = 35
}

data object DoubleZeroBet : Bet {
    override val name = "00"
    override val matchingNumbers = setOf(DOUBLE_ZERO)
    override val payoutMultiplier = 35
}

class DozenBet(
    base: Int,
) : Bet {
    override val name = "Dozen $base - ${base + 11}"
    override val matchingNumbers = (base..(base + 11)).toSet()
    override val payoutMultiplier = 3
}

class ColumnBet(
    base: Int,
) : Bet {
    override val name = "Column $base - ${base + 33}"
    override val matchingNumbers = BASE_NUMBERS.filter { (it % 3) == (base % 3) }.toSet()
    override val payoutMultiplier: Int = 3
}

data object LowNumberBet : Bet {
    override val name = "Low numbers"
    override val matchingNumbers = LOW_NUMBERS
    override val payoutMultiplier = 2
}

data object HighNumberBet : Bet {
    override val name = "High number"
    override val matchingNumbers = HIGH_NUMBERS
    override val payoutMultiplier = 2
}

data object EvenNumberBet : Bet {
    override val name = "Even number"
    override val matchingNumbers = EVEN_NUMBERS
    override val payoutMultiplier = 2
}

data object OddNumberBet : Bet {
    override val name = "Odd number"
    override val matchingNumbers = ODD_NUMBERS
    override val payoutMultiplier = 2
}

data object RedNumberBet : Bet {
    override val name = "Red number"
    override val matchingNumbers = RED_NUMBERS
    override val payoutMultiplier = 2
}

data object BlackNumberBet : Bet {
    override val name = "Black number"
    override val matchingNumbers = BLACK_NUMBERS
    override val payoutMultiplier = 2
}
