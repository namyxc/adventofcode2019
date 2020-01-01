import com.sun.org.apache.xpath.internal.operations.Bool





fun String.toProgram(index: Int = 0): Puzzle5.ProgramState {
    val program = this.split(',').filter { it.isNotEmpty() }.map { it.toInt() }
    return Puzzle5.ProgramState(index,program)
}

object Puzzle5 {

    val print = false

    interface OpCodeFunctions {
        fun getNumberOfParams(): Int
    }

    enum class OpCodeType(val code:Int): OpCodeFunctions{
        Add(1) {
            override fun getNumberOfParams() = 3
        },
        Multiple(2){
            override fun getNumberOfParams() = 3
        },
        Input(3){
            override fun getNumberOfParams() = 1
        },
        Output(4){
            override fun getNumberOfParams() = 1
        },
        JumpIfTrue(5){
            override fun getNumberOfParams() = 2
        },
        JumpIfFalse(6){
            override fun getNumberOfParams() = 2
        },
        LessThan(7){
            override fun getNumberOfParams() = 3
        },
        Equals(8){
            override fun getNumberOfParams() = 3
        },
        Halt(99){
            override fun getNumberOfParams() = 0
        }
    }

    enum class ParamType(val code: Int){
        Position(0),
        Value(1)
    }

    data class Instruction(val opcodeWithParamTypes: Int){
        val opCodeType: OpCodeType by lazy {OpCodeType.values().first { it.code ==  opcodeWithParamTypes.rem(100)}}
        val params: List<ParamType> by lazy {
            val numberOfParams = opCodeType.getNumberOfParams()
            var p = MutableList(numberOfParams){ParamType.Position}
            var paramsInt: Int = opcodeWithParamTypes / 100
            var index = numberOfParams - 1
            while (paramsInt > 0){
                val param = paramsInt.rem(10)
                val type = ParamType.values().first{it.code == param}
                p[index] = type
                index--
                paramsInt /= 10
            }
            p
        }

        override fun toString(): String {
            return "Instruction(opcodeWithParamTypes=$opcodeWithParamTypes); $opCodeType\n $params"
        }
    }

    data class ProgramState(val index: Int, val program: List<Int>){
        fun getInstruction() = Instruction(program[index])
        fun isHalt() = getInstruction().opCodeType == OpCodeType.Halt
    }

    fun runInstruction(programState: ProgramState) : ProgramState {
        val instruction = programState.getInstruction()
        val opCodeType = instruction.opCodeType
        val program = programState.program.toMutableList()
        var index = programState.index
        when (opCodeType){
            OpCodeType.Input -> {
                println("Need input")
                val input = readLine()?.toInt() ?: 0
                val param = program[index + 1]
                val indexToSave = if (instruction.params.first() == ParamType.Value)  program[param] else param
                program[indexToSave] = input
                index += instruction.opCodeType.getNumberOfParams() + 1
            }
            OpCodeType.Output -> {
                val param = program[index + 1]
                val number = if (instruction.params.first() == ParamType.Value)  param else  program[param]
                println("Output: $number")
                index += instruction.opCodeType.getNumberOfParams() + 1
            }
            OpCodeType.Multiple, OpCodeType.Add,
            OpCodeType.LessThan, OpCodeType.Equals-> {
                val nextData = program[index + 1]
                val nextNextData = program[index + 2]
                val resultIndex = program[index + 3]
                val firstParam = if (instruction.params[2] == ParamType.Value) nextData else program[nextData]
                val secondParam = if (instruction.params[1] == ParamType.Value) nextNextData else program[nextNextData]
                val result = when (opCodeType){
                    OpCodeType.Multiple -> firstParam * secondParam
                    OpCodeType.Add -> firstParam + secondParam
                    OpCodeType.LessThan -> if (firstParam < secondParam) 1 else 0
                    OpCodeType.Equals -> if (firstParam == secondParam) 1 else 0
                    else -> 0
                }
                debug("$opCodeType: $firstParam . $secondParam = $result save to $resultIndex")

                program[resultIndex] = result
                index += instruction.opCodeType.getNumberOfParams() + 1
            }
            OpCodeType.JumpIfTrue, OpCodeType.JumpIfFalse -> {
            val inputIndex = if (instruction.params[1] == ParamType.Value) index + 1 else program[index + 1]
            val needJump =
                (opCodeType == OpCodeType.JumpIfTrue && program[inputIndex] != 0)
                        || (opCodeType == OpCodeType.JumpIfFalse && program[inputIndex] == 0)
            debug("test if ${program[inputIndex]} is 0")
            if (needJump){
                val jumpIndex = if (instruction.params[0] == ParamType.Value) index + 2 else program[index + 2]
                index = program[jumpIndex]
                debug("jump to $jumpIndex")
            }else {
                index += instruction.opCodeType.getNumberOfParams() + 1
                debug("continue here: $index")
            }
        }
        }
        return ProgramState(index, program)
    }

    private fun debug(text: String){
        if (print)
            println(text)
    }

    fun runProgram(programState: ProgramState): ProgramState{
        var innerState = programState
        while (!innerState.isHalt()){
            innerState = runInstruction(innerState)
        }
        return innerState
    }

    fun runProgramOld(input: List<Int>): List<Int>{
        var program = input.toMutableList()
        var index = 0
        loop@ while (index < program.size){
            debug("******************$index***************************")
            debug("program to $index: ${program.take(index)}")
            debug("program from $index: ${program.drop(index)}")
            val instruction = Instruction(program[index])
            debug(instruction.toString())
            val opCodeType = instruction.opCodeType
            when (opCodeType) {
                OpCodeType.Input -> {
                    println("Need input")
                    val number = readLine()?.toInt() ?: 0
                    val param = program[index + 1]
                    val indexToSave = if (instruction.params.first() == ParamType.Value)  program[param] else param
                    program[indexToSave] = number
                    debug("input: $number saved to position $indexToSave")
                    index += instruction.opCodeType.getNumberOfParams() + 1
                }
                OpCodeType.Output -> {
                    val param = program[index + 1]
                    val number = if (instruction.params.first() == ParamType.Value)  param else  program[param]
                    println("Output: $number")
                    index += instruction.opCodeType.getNumberOfParams() + 1
                }
                OpCodeType.Multiple, OpCodeType.Add -> {
                    val nextData = program[index + 1]
                    val nextNextData = program[index + 2]
                    val resultIndex = program[index + 3]
                    val firstParam = if (instruction.params[2] == ParamType.Value) nextData else program[nextData]
                    val secondParam = if (instruction.params[1] == ParamType.Value) nextNextData else program[nextNextData]
                    val result = if (opCodeType == OpCodeType.Multiple) firstParam * secondParam else firstParam + secondParam
                    debug("$opCodeType: $firstParam . $secondParam = $result save to $resultIndex")

                    program[resultIndex] = result
                    index += instruction.opCodeType.getNumberOfParams() + 1
                }
                OpCodeType.JumpIfTrue, OpCodeType.JumpIfFalse -> {
                    val inputIndex = if (instruction.params[1] == ParamType.Value) index + 1 else program[index + 1]
                    val needJump =
                        (opCodeType == OpCodeType.JumpIfTrue && program[inputIndex] != 0)
                                || (opCodeType == OpCodeType.JumpIfFalse && program[inputIndex] == 0)
                    debug("test if ${program[inputIndex]} is 0")
                    if (needJump){
                        val jumpIndex = if (instruction.params[0] == ParamType.Value) index + 2 else program[index + 2]
                        index = jumpIndex
                        debug("jump to $jumpIndex")
                    }else {
                        index += instruction.opCodeType.getNumberOfParams() + 1
                        debug("continue here: $index")
                    }
                }
                OpCodeType.Halt -> {
                    break@loop
                }
            }



        }
        return program
    }

    @JvmStatic
    fun main(args : Array<String>) {
        val input =
            Puzzle5::class.java.getResource("puzzle5.txt").readText().trim()

        val output = runProgram(input.toProgram())
    }

    }