object Puzzle2 {

    fun runProgram(input: List<Int>): List<Int>{
        var program = input.toMutableList()
        loop@ for (i in 0 until program.size step 4){
            val operator = program[i]
            when (operator){
                1,2 -> {
                    val idx1 = program[i+1]
                    val idx2 = program[i+2]
                    val op1 = program[idx1]
                    val op2 = program[idx2]
                    val resultIndex = program[i+3]
                    val result = if (operator == 1)  op1 + op2 else op1*op2
                    program[resultIndex] = result
                }
                99 -> break@loop
                else -> print("unknown operator: $operator")
            }
        }
        return program
    }

    @JvmStatic
    fun main(args : Array<String>) {
        val input =
            Puzzle2::class.java.getResource("puzzle2.txt").readText().trim().split(',').filter { it.isNotEmpty() }.map { it.toInt() }

        var mutableInput = input.toMutableList()
        mutableInput[1] = 12
        mutableInput[2] = 2
        val output = runProgram(mutableInput)
        println("part1: ${output[0]}")

        loopNoun@ for (noun in 0 until 100 ){
            for (verb in 0 until 100){
                var mutableInput = input.toMutableList()
                mutableInput[1] = noun
                mutableInput[2] = verb
                val output = runProgram(mutableInput)
                if (output[0] == 19690720) {
                    println("part2: ${noun * 100 + verb}")
                    break@loopNoun
                }
            }
        }
    }
}