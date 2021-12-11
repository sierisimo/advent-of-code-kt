import java.util.*

data class BingoBox(
    val number: Int,
    var checked: Boolean = false
) {
    override fun toString(): String {
        return "($number, $checked)"
    }
}

class Board(numbers: List<String>) {
    private val value: List<List<BingoBox>> =
        numbers.map { row -> row.split(" ").filter(String::isNotEmpty).map { BingoBox(it.toInt()) } }

    var winningNumber: Int = 0

    private val rows = value

    private val columns: List<List<BingoBox>>
        get() = value.mapTranspose { it }

    val hasBingo: Boolean
        get() = rows.any { row -> row.all { it.checked } } || columns.any { column -> column.all { it.checked } }

    val score: Int
        get() = rows.flatten()
            .filter { !it.checked }
            .sumOf { it.number } * winningNumber

    fun mark(number: Int): Boolean {
        value.forEach { row ->
            row.find { it.number == number }?.checked = true
        }
        val winner = hasBingo
        if (winner) winningNumber = number

        return winner
    }

    override fun toString(): String {
        return value.joinToString(separator = "") { it.joinToString(postfix = "\n") }
    }
}

fun main() {
    tailrec fun recursiveBoards(
        input: List<String>,
        boards: List<Board> = emptyList(),
        lineAcc: List<String> = emptyList()
    ): List<Board> {
        if (input.isEmpty()) return boards + Board(lineAcc)
        return when {
            input.first().isEmpty() -> recursiveBoards(input.drop(1), boards + Board(lineAcc))
            else -> recursiveBoards(input.drop(1), boards, lineAcc + input.first())
        }
    }

    fun part1(input: List<String>): Int {
        val numbers = input.first().split(",").map(String::toInt)
        val boards = recursiveBoards(input.drop(2))

        numbers.foldWhile(false, { !it }) { _, n ->
            boards.fold(false) { acc, board -> if (acc) true else board.mark(n) }
        }

        return boards.find { it.hasBingo }?.score ?: -1
    }

    fun part2(input: List<String>): Int {
        val numbers = input.first().split(",").map(String::toInt)
        val boards = recursiveBoards(input.drop(2))
        val winningBoards = Stack<Board>()
        numbers.foldWhile(false, { !it }) { _, n ->
            boards.forEach {
                if (it.mark(n) && !winningBoards.contains(it)) winningBoards.push(it)
            }
            boards.all { it.hasBingo }
        }

        return winningBoards.pop().score
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
