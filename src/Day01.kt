fun main() {
  // Iterative solution, it abuses the usage of reduce
  fun countIncrements(list: List<Int>): Int {
    var count = 0
    list.reduce { prev, next ->
      if (next > prev) count++
      next
    }
    return count
  }

  // A little more functional solution but with a small cost
  fun countWithZip(list: List<Int>): Int = list.zipWithNext()
    .filter { it.second > it.first }
    .size
  
  //CoRecursive solution as I'm forcing myself to use more corecursion
  tailrec fun recursiveCount(
    list: List<Int>,
    previous: Int = Int.MAX_VALUE,
    acc: Int = 0,
  ): Int {
    if (list.isEmpty()) return acc

    val first = list.first()
    val nextAcc = if (first > previous) acc + 1 else acc
    return recursiveCount(
      list.drop(1),
      first,
      nextAcc
    )
  }

  fun part1(input: List<String>): Int {
    //Haven't checked which solution is more efficient. Does it matter?
    //return countIncrements(input.map(String::toInt))
//    return recursiveCount(
    return countWithZip(
      input.map(String::toInt)
    )
  }

  fun iterativeMapToSums(list: List<Int>): List<Int> {
    val rem = list.size % 3
    val limit = list.size - rem
    val resultList = mutableListOf<Int>()
    for (i in 0 until limit) {
      resultList.add(list[i] + list[i + 1] + list[i + 2])
    }

    return resultList
  }

  fun part2(input: List<String>): Int {
    return recursiveCount(
      iterativeMapToSums(
        input.map(String::toInt)
      )
    )
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day01_test")
  check(part1(testInput) == 7)

  val input = readInput("Day01")
  println(part1(input))
  println(part2(input))
}
