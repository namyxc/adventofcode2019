import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream


internal class Puzzle5Test{
    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalIn = System.`in`
    private val originalErr = System.err

    @BeforeEach
    fun setUpStreams() {
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }

    @AfterEach
    fun restoreStreams() {
        System.setIn(originalIn)
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    fun output() = outContent.toString().split("\n").filter { it.isNotEmpty() }


    @Test
    fun instructionReaderTest(){
        val instruction = Puzzle5.Instruction(1002)
        assertEquals(Puzzle5.OpCodeType.Multiple, instruction.opCodeType)
        val params = instruction.params
        assertEquals(Puzzle5.OpCodeType.Multiple.getNumberOfParams(),params.size)
        assertEquals(Puzzle5.ParamType.Position,params[0])
        assertEquals(Puzzle5.ParamType.Value,params[1])
        assertEquals(Puzzle5.ParamType.Position,params[2])
    }

    @Test
    fun inputTest(){
        val inputProgram = "3,0"
        val `in` = ByteArrayInputStream("100".toByteArray())
        System.setIn(`in`)

        val programState = Puzzle5.runInstruction(inputProgram.toProgram())
        val expectedProgramState = "100,0".toProgram(2)
        assertEquals(expectedProgramState, programState)
    }

    @Test
    fun outputTest(){
        val programString = "4,0"
        val programState = Puzzle5.runInstruction(programString.toProgram())
        val expectedProgramState = programString.toProgram(2)

        assertEquals(expectedProgramState, programState)
        assertEquals(arrayListOf("Output: 4"), output())
    }

    @ParameterizedTest(name = "test for {0} input: {1} output {2} index {3} ")
    @CsvSource(delimiter = ';',
        value = [
            "multiply;1002,4,3,4,33;1002,4,3,4,99;4",
            "jumpIfTrue;5,0,5;5,0,5;5",
            "jumpIfFalse;6,3,1,0;6,3,1,0;1",
            "lessThan;7,1,2,0;1,1,2,0;4",
            "equals;8,2,2,0;1,2,2,0;4"
        ]
    )
    fun numericTests(testName: String, input: String, output: String, expectedIndex: Int){
        val inputState = input.toProgram()
        val expectedState = output.toProgram(expectedIndex)

        val outputState = Puzzle5.runInstruction(inputState)
        assertEquals(expectedState, outputState)
    }

    @ParameterizedTest(name = "test for {3} inputProgram: {0} input number {1} output number {2} ")
    @CsvSource(delimiter = ';',
        value = [
            "3,9,8,9,10,9,4,9,99,-1,8;8;1;Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).",
            "3,9,8,9,10,9,4,9,99,-1,8;6;0;Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).",

            "3,3,1108,-1,8,3,4,3,99;8;1;Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).",
            "3,3,1108,-1,8,3,4,3,99;6;0;Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).",

            "3,3,1107,-1,8,3,4,3,99;6;1;Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not).",
            "3,3,1107,-1,8,3,4,3,99;8;0;Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not).",

            "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9;100;1;take an input, then output 0 if the input was zero or 1 if the input was non-zero (using position mode)",
            "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9;0;0;take an input, then output 0 if the input was zero or 1 if the input was non-zero (using position mode)",

            "3,3,1105,-1,9,1101,0,0,12,4,12,99,1;100;1;take an input, then output 0 if the input was zero or 1 if the input was non-zero (using immediate mode)",
            "3,3,1105,-1,9,1101,0,0,12,4,12,99,1;0;0;take an input, then output 0 if the input was zero or 1 if the input was non-zero (using immediate mode)",


            "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99;"+
                    "6;999;output 999 if the input value is below 8",
            "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99;"+
                    "8;1000;output 1000 if the input value is equal to 8",
            "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99;"+
                    "9;1001;output 1001 if the input value is greater than 8"

        ]
    )
    fun inputOutputProgramTest(inputProgram: String, inputNumber: Int, outputNumber: Int,testName: String){
        val inputState = inputProgram.toProgram()
        val `in` = ByteArrayInputStream(inputNumber.toString().toByteArray())
        System.setIn(`in`)
        val outputState = Puzzle5.runProgram(inputState)

        assertEquals(arrayListOf("Need input","Output: $outputNumber"), output())
    }


}