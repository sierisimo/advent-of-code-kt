import java.lang.Math.abs

/**
 * Simple abstraction of a point, to avoid renaming first/second on Pair, and make things more domain aware
 */
data class Point(val x: Int, val y: Int)

/**
 * A Vent is a simple representation of the problem, is a line that starts in a Point(x1,y1) and end in Point(x2,y2)
 */
data class Vent(private val pointA: Point, private val pointB: Point) {
    /**
     * Same X means it only changes in Y, then is a vertical Vent
     */
    val isVertical: Boolean
        get() = pointA.x == pointB.x

    /**
     * Same Y means it only changes in X, then is an horizontal Vent
     */
    val isHorizontal: Boolean
        get() = pointA.y == pointB.y

    /**
     * A little more tricky: If (y2 - y1)/(x2 - x1) == 1 it means 45 degrees on the inclination of the line
     * but given we not order points from the beginning it can also be -1,
     * which mean it goes from right to left and from bottom to top.
     * That's why the need of an abs
     */
    val isDiagonal: Boolean
        get() {
            val (start, end) = getStartAndEnd()
            return (!isVertical && !isHorizontal) && abs((end.y - start.y) / (end.x - start.x)) == 1
        }

    /**
     * Crossing points are calculated differently, for horizotnal and vertical is just moving on one axis.
     * Diagonal require to do simultaneous steps depending on the direction.
     */
    fun getCrossingPoints(): List<Point> =
        if (isDiagonal) getDiagonalPoints() else getVerticalOrHorizontalPoints()

    private fun getStartAndEnd(): Pair<Point, Point> =
        if (pointA.y < pointB.y || pointA.x < pointB.x) Pair(pointA, pointB)
        else Pair(pointB, pointA)

    private fun getVerticalOrHorizontalPoints(): List<Point> {
        val (start, end) = getStartAndEnd()

        return (start.x..end.x).map { x ->
            (start.y..end.y).map { y ->
                Point(x, y)
            }
        }.flatten()
    }

    private fun getDiagonalPoints(): List<Point> {
        val (horizontalStep, verticalStep) = when {
            pointA.x < pointB.x && pointA.y < pointB.y -> Pair(1, 1) // left->right :: top->down
            pointA.x < pointB.x && pointA.y > pointB.y -> Pair(1, -1) // left->right :: down->top
            pointA.x > pointB.x && pointA.y < pointB.y -> Pair(-1, 1)// right->left :: top->down
            pointA.x > pointB.x && pointA.y > pointB.y -> Pair(-1, -1)// right->left :: down->top
            else -> throw IllegalStateException("Weird move my friend. Not a 45 degrees diagonal.")
        }

        val points = mutableListOf<Point>()

        var x = pointA.x
        var y = pointA.y
        while (x != pointB.x && y != pointB.y) {
            points.add(Point(x, y))
            x += horizontalStep
            y += verticalStep
        }
        points.add(pointB)

        return points
    }
}

fun main() {
    fun pointsFrom(input: String): Point = input.delimitedToInt(",").let { Point(it.first(), it.last()) }

    fun ventFromString(input: List<String>): List<Vent> = input.map { it.splitMap(" -> ", ::pointsFrom) }
        .map { Vent(it.first(), it.last()) }

    fun part1(input: List<String>): Int {
        val vents = ventFromString(input)
            .filter { it.isHorizontal || it.isVertical }

        return vents.map(Vent::getCrossingPoints)
            .flatten()
            .groupBy { it }
            .filter { it.value.size > 1 }.size
    }

    fun part2(input: List<String>): Int {
        val vents = ventFromString(input)
            .filter { it.isHorizontal || it.isVertical || it.isDiagonal }


        return vents.map(Vent::getCrossingPoints)
            .flatten()
            .groupBy { it }
            .filter { it.value.size > 1 }.size
    }


    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
