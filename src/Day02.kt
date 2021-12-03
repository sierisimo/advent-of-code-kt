/**
 * Representation of the position for our submarine.
 * It could be simple variables... but I like living by types
 */
data class Submarine(
    val depth: Int = 0,
    val horizontal: Int = 0,
    val aim: Int = 0,
    val useAim: Boolean = false,
)

/**
 * Why not using the same instance and just modify the properties?
 * Well... actually that would make sense, to make things private and modify the original submarine...
 * But I like immutability, and it would have meant the extension will modify the submarine and then return `this`
 * and then you ask: "well... then it does not need to be an extension! it can be a simple method..."
 *
 * Yes, but once again, I like immutable things... so shrug. this is one of those cases where I'm abusing language.
 *
 * By the end for how the fold works, it doesn't matter if I return a new instance or the mutated instance, as long as
 * a [Submarine] is present!
 */
fun Submarine.moveForward(value: Int): Submarine = Submarine(
    depth + if (useAim) aim * value else 0, // Looks a little ugly, but I like it more thant having two different calls
    horizontal + value,
    aim,
    useAim
)

fun Submarine.goDeep(value: Int): Submarine =
    if (useAim) Submarine(depth, horizontal, aim + value, true)
    else Submarine(depth + value, horizontal)

fun Submarine.goNarrow(value: Int): Submarine =
    if (useAim) Submarine(depth, horizontal, aim - value, true)
    else Submarine(depth - value, horizontal)

/**
 * Have a type instead of strings, less error-prone. Also, an overthinking of the base problem for readability.
 */
enum class Direction {
    Forward,
    Up,
    Down,
}

fun directionOf(from: String): Direction = when (from) {
    "forward" -> Direction.Forward
    "up" -> Direction.Up
    "down" -> Direction.Down
    else -> throw IllegalArgumentException("Unknown Direction")
}

data class Instruction(
    val direction: Direction,
    val value: Int,
)

////// Simply mappings for contextualize the problem, not necessary, just nice readability for myself. //////
fun instructionOf(instructionParts: List<String>): Instruction =
    Instruction(directionOf(instructionParts.first()), instructionParts.last().toInt())

fun main() {
    fun List<String>.mapToInstructions(): List<Instruction> = map { it.split(" ") }.map(::instructionOf)

    fun nextPositionFromInstruction(submarine: Submarine, instruction: Instruction) = when (instruction.direction) {
        Direction.Forward -> submarine.moveForward(instruction.value)
        Direction.Down -> submarine.goDeep(instruction.value)
        Direction.Up -> submarine.goNarrow(instruction.value)
    }

    fun solve(list: List<String>, usingAim: Boolean = false): Int {
        val submarine = list.mapToInstructions().fold(
            Submarine(useAim = usingAim),
            ::nextPositionFromInstruction
        )

        return submarine.depth * submarine.horizontal
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(solve(testInput) == 150)
    check(solve(testInput, true) == 900)

    val input = readInput("Day02")
    println(solve(input))
    println(solve(input, usingAim = true))
}
