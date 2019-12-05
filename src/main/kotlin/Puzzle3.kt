import kotlin.math.*

object Puzzle3 {
    enum class Orientation {
        VERTICAL,
        HORIZONTAL
    }

    data class Point(
        val x: Int,
        val y: Int
    )

    data class Line(
        val start: Point,
        val end: Point,
        val lengthBeforeLine: Int = 0) {
        val length: Int by lazy {abs(start.x-end.x) + abs(start.y-end.y)}
        val orientation: Orientation by lazy { if (start.x == end.x) Orientation.VERTICAL else Orientation.HORIZONTAL }
        val top: Int by lazy { max(start.y, end.y) }
        val bottom: Int by lazy { min(start.y, end.y) }
        val left: Int by lazy {min(start.x, end.x)}
        val right: Int by lazy {max(start.x, end.x)}
        fun distanceToPoint(p: Point) = abs(p.x - start.x) + abs(p.y - start.y)
    }

    fun readInput(input: String): List<Line> {
        var lines = mutableListOf<Line>()
        var point = Point(0,0)
        var length = 0
        val moves = input.trim().split(',').filter { it.isNotEmpty() }
        for (move in moves){
            val direction = move.first()
            val distance = move.substring(1).toInt()
            val endPoint = Point(
                when (direction) {
                    'R' -> point.x + distance
                    'L' -> point.x - distance
                    else -> point.x
                },
                when (direction) {
                    'U' -> point.y + distance
                    'D' -> point.y - distance
                    else -> point.y
                }

            )
            val line = Line(point, endPoint, length)
            lines.add(line)
            length += line.length
            point = endPoint
        }
        return lines
    }

    fun getIntersection(line: Line, otherLine: Line): Point? {
        if (line.orientation == otherLine.orientation) return null

        if (line.orientation == Orientation.VERTICAL){
            if (
                line.top >= otherLine.top && line.bottom <= otherLine.bottom
                && otherLine.left <= line.left && otherLine.right >= line.right
            ){
                return Point(line.start.x, otherLine.start.y)
            } else return null
        }else{
            if (
                otherLine.top >= line.top && otherLine.bottom <= line.bottom
                && line.left <= otherLine.left && line.right >= otherLine.right
            ){
                return Point(otherLine.start.x, line.start.y)
            } else return null
        }
    }

    fun getClosestIntersectionByManhattan(segments: List<Line>, otherSegments: List<Line>): Int? {
        var intersections = mutableListOf<Point>()
        for (segment in segments){
            for (otherSegment in otherSegments){
                val intersecion = getIntersection(segment, otherSegment)
                if (intersecion != null)
                    intersections.add(intersecion)
            }
        }
        return intersections.filterNot { p -> p.x == 0 && p.y == 0 }.map { p -> abs(p.x) + abs(p.y) }.min()
    }


    fun getClosestIntersectionBySteps(segments: List<Line>, otherSegments: List<Line>): Int? {
        var intersections = mutableListOf<Int>()
        for (segment in segments){
            for (otherSegment in otherSegments){
                val intersecion = getIntersection(segment, otherSegment)
                if (intersecion != null) {

                    val steps = segment.lengthBeforeLine +
                        otherSegment.lengthBeforeLine +
                        segment.distanceToPoint(intersecion) +
                        otherSegment.distanceToPoint(intersecion)
                    intersections.add(steps)
                }
            }
        }
        return intersections.filterNot { d -> d == 0 }.min()
    }

    @JvmStatic
    fun main(args : Array<String>) {
        val input =
            Puzzle3::class.java.getResource("puzzle3.txt").readText().trim().split('\n').filter { it.isNotEmpty() }
        val firstLine = readInput(input[0])
        val secondLine = readInput(input[1])
        val distanceByManhattan = getClosestIntersectionByManhattan(firstLine, secondLine)
        val distanceBySteps = getClosestIntersectionBySteps(firstLine, secondLine)
        println("distanceByManhattan: $distanceByManhattan")
        println("distanceBySteps: $distanceBySteps")
    }
}