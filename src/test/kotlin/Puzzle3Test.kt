import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Puzzle3Test{

    @ParameterizedTest(name = "input: {0} output {1} ")
    @CsvSource(delimiter = ';',
        value = [
            "R10;1;10;0",
            "L10;1;-10;0",
            "U10;1;0;10",
            "D10;1;0;-10",

            "R10,U10;2;10;10"
        ]
    )
    fun readSegments(input:String, expectedSize: Int, lastX: Int, lastY: Int){
        val lines = Puzzle3.readInput(input)
        assertEquals(expectedSize, lines.size)
        val lastLine = lines.last()
        assertEquals(Puzzle3.Point(lastX, lastY), lastLine.end)
    }

    @Test
    fun testOrientation(){
        val line1 = Puzzle3.Line(Puzzle3.Point(-1,0), Puzzle3.Point(1,0))
        val line2 = Puzzle3.Line(Puzzle3.Point(0,-1), Puzzle3.Point(0,1))
        assertEquals(Puzzle3.Orientation.HORIZONTAL, line1.orientation)
        assertEquals(Puzzle3.Orientation.VERTICAL, line2.orientation)
    }

    @Test
    fun getIntersection() {
        val line1 = Puzzle3.Line(Puzzle3.Point(-1,0), Puzzle3.Point(1,0))
        val line2 = Puzzle3.Line(Puzzle3.Point(0,-1), Puzzle3.Point(0,1))
        val intersection = Puzzle3.getIntersection(line1, line2)
        val expectedIntersection = Puzzle3.Point(0,0)
        assertEquals(expectedIntersection, intersection)
    }

    @ParameterizedTest(name = "line1: {0} line2 {1} ")
    @CsvSource(delimiter = ';',
        value = [
            "R8,U5,L5,D3;U7,R6,D4,L4;6",
            "R75,D30,R83,U83,L12,D49,R71,U7,L72;U62,R66,U55,R34,D71,R55,D58,R83;159",
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51;U98,R91,D20,R16,D67,R40,U7,R15,U6,R7;135"
        ]
    )
    fun getClosestIntersection(firstLine: String, secondLine: String, expectedDistance: Int) {
        val line1 = Puzzle3.readInput(firstLine)
        val line2 = Puzzle3.readInput(secondLine)
        val distance = Puzzle3.getClosestIntersectionByManhattan(line1, line2)
        assertEquals(expectedDistance, distance)
    }

    @ParameterizedTest(name = "Start: {0},{1} end {2},{3}, point: {4},{5}, expected distance: {6} ")
    @CsvSource(value = [
            "0,0,0,10,0,2,2",
            "0,0,0,10,0,8,8",
            "0,0,10,0,3,0,3"
        ]
    )
    fun distanceToPoint(startX: Int, startY: Int, endX: Int, endY: Int, pointX: Int, pointY: Int, expectedDistance: Int){
        val segment = Puzzle3.Line(Puzzle3.Point(startX, startY), Puzzle3.Point(endX, endY))
        val point = Puzzle3.Point(pointX,pointY)
        val calculatedDistance = segment.distanceToPoint(point)
        assertEquals(expectedDistance, calculatedDistance)
    }

    @ParameterizedTest(name = "line1: {0} line2 {1} ")
    @CsvSource(delimiter = ';',
        value = [
            "R8,U5,L5,D3;U7,R6,D4,L4;30",
            "R75,D30,R83,U83,L12,D49,R71,U7,L72;U62,R66,U55,R34,D71,R55,D58,R83;610",
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51;U98,R91,D20,R16,D67,R40,U7,R15,U6,R7;410"
        ]
    )
    fun getClosestIntersectionBySteps(firstLine: String, secondLine: String, expectedDistance: Int) {
        val line1 = Puzzle3.readInput(firstLine)
        val line2 = Puzzle3.readInput(secondLine)
        val distance = Puzzle3.getClosestIntersectionBySteps(line1, line2)
        assertEquals(expectedDistance, distance)
    }
}