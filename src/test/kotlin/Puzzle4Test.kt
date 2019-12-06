import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Puzzle4Test {

    @ParameterizedTest(name = "Number: {0} isOk: {1} ")
    @CsvSource(value = [
        "111111,true",
        "223450,false",
        "123789,false"
    ]
    )
    fun checknumber(number: Int, expected: Boolean) {
        assertEquals(Puzzle4.checknumber(number), expected)
    }


    @ParameterizedTest(name = "Number: {0} isOk: {1} ")
    @CsvSource(value = [
        "112233,true",
        "123444,false",
        "111122,true"
    ]
    )
    fun checknumberNoTriplets(number: Int, expected: Boolean) {
        assertEquals(Puzzle4.checknumberNoTriplets(number), expected)
    }
}