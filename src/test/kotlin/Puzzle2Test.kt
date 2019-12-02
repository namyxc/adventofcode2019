import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Puzzle2Test{

    /*
    *
    1,0,0,0,99 becomes 2,0,0,0,99 (1 + 1 = 2).
2,3,0,3,99 becomes 2,3,0,6,99 (3 * 2 = 6).
2,4,4,5,99,0 becomes 2,4,4,5,99,9801 (99 * 99 = 9801).
1,1,1,4,99,5,6,0,99 becomes 30,1,1,4,2,5,6,0,99.
    * */

        @ParameterizedTest(name = "input: {0} output {1} ")
        @CsvSource(delimiter = ';',
            value = ["1,0,0,0,99;2,0,0,0,99",
            "2,3,0,3,99;2,3,0,6,99",
            "2,4,4,5,99,0;2,4,4,5,99,9801",
            "1,1,1,4,99,5,6,0,99;30,1,1,4,2,5,6,0,99"]
        )
        fun runProgram(input: String, expectedResult: String) {
            val runned = Puzzle2.runProgram(input.split(',').filter { it.isNotEmpty() }.map { it.toInt() }).joinToString(",")
            assertEquals(expectedResult, runned) {
                "$runned should equal $expectedResult"
            }
        }
}