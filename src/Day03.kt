import kotlin.math.pow

data class GammaEpsilon(
    val gamma: String = "",
    val epsilon: String = ""
)

fun GammaEpsilon.toDecimals(): Pair<Int, Int> = Pair(
    gamma.binaryToDecimal(),
    epsilon.binaryToDecimal()
)

fun String.binaryToDecimal(): Int = foldRight(Pair(0, 0)) { c, acc ->
    Pair(
        acc.first + 1,
        acc.second +
                if (c == '0') 0 else 2.0.pow(acc.first).toInt()
    )
}.second

fun main() {
    fun part1(input: List<String>): Int {
        val gammaEpsilonValues = input.fold(emptyList<Pair<Int, Int>>()) { accList, str ->
            val strPairs = str.fold(emptyList<Pair<Int, Int>>()) { pairList, char ->
                pairList + Pair(if (char == '0') 1 else 0, if (char == '1') 1 else 0)
            }
            if (accList.isEmpty()) strPairs else accList.zip(strPairs) { xPair, yPair ->
                Pair(xPair.first + yPair.first, xPair.second + yPair.second)
            }
        }.fold(GammaEpsilon()) { gammaEpsilon, pair ->
            GammaEpsilon(
                gammaEpsilon.gamma + if (pair.first > pair.second) "0" else "1",
                gammaEpsilon.epsilon + if (pair.first > pair.second) "1" else "0"
            )
        }.toDecimals()

        return gammaEpsilonValues.fold(Int::times)
    }

//    test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)

    val input = readInput("Day03")
    println(part1(input))
}
