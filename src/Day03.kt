import kotlin.math.pow

/*
 * I really dislike this solutions, it's merely brute force and just hacks over hacks... if you have any suggestions
 * feel free to open an issue with hints or ideas.
 */

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
        acc.first + 1, // Power number
        acc.second +
                if (c == '0') 0 else 2.0.pow(acc.first).toInt()
    )
}.second

val Pair<Int, Int>.biggest: Int
    get() = if (first > second) 0 else 1

val Pair<Int, Int>.smallest: Int
    get() = if (first <= second) 0 else 1

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

    fun part2(input: List<String>): Int {
        fun getZeroOneCountPair(list: List<String>, position: Int): Pair<Int, Int> =
            list.fold(Pair(0, 0)) { acc, str ->
                Pair(acc.first + if (str[position] == '0') 1 else 0, acc.second + if (str[position] == '1') 1 else 0)
            }

        tailrec fun findNumberByCount(
            l: List<String>,
            filterBy: (Int, Pair<Int, Int>) -> Boolean,
            position: Int = 0
        ): List<String> {
            if (l.size == 1) return l

            val zeroOneCountPair = getZeroOneCountPair(l, position)
            return findNumberByCount(
                l.filter { filterBy(it[position].digitToInt(), zeroOneCountPair) },
                filterBy,
                position + 1
            )
        }

        val byBiggest = findNumberByCount(input, { d, p -> d == p.biggest }).first().binaryToDecimal()
        val byLowest = findNumberByCount(input, { d, p -> d == p.smallest }).first().binaryToDecimal()

        return byBiggest * byLowest
    }

//    test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
