import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <A, B, C> Pair<A, B>.fold(f: (A, B) -> C): C = f(first, second)


fun <T, R> List<List<T>>.mapTranspose(mapFun: (T) -> R): List<List<R>> {
    fun <T, R> List<T>.splitToLists(mapFun: (T) -> R): List<List<R>> =
        fold(emptyList()) { listAcc, element ->
            listAcc + listOf(listOf(mapFun(element)))
        }

    return fold(emptyList()) { acc, str ->
        val resultList = str.splitToLists(mapFun)
        if (acc.isEmpty()) resultList
        else acc.zip(resultList) { a, b -> a + b }
    }
}

tailrec fun <T, R> List<T>.foldWhile(accumulator: R, predicate: (R) -> Boolean, transformation: (R, T) -> R): R {
    return when {
        this.isEmpty() -> accumulator
        else -> {
            val nextAcc = transformation(accumulator, first())
            val tail = this.drop(1)
            if (predicate(nextAcc)) tail.foldWhile(nextAcc, predicate, transformation)
            else accumulator
        }
    }
}

fun String.delimitedToInt(delimiter: String): List<Int> = splitMap(delimiter) { it.toInt() }

fun <R> CharSequence.splitMap(
    delimiter: String,
    mapFun: (String) -> R
): List<R> = split(delimiter).map(mapFun)
