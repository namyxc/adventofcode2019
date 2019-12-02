import java.lang.Math.floor

object Puzzle1 {

        fun calculateFuel(mass: Int) = floor(mass/3.0).toInt() - 2

        fun calculateFuelRecursive(mass: Int): Int{
                val fuel = calculateFuel(mass)
                return if (fuel <= 0 ) 0
                else fuel + calculateFuelRecursive(fuel)
        }

        @JvmStatic
        fun main(args : Array<String>) {
                val input = Puzzle1::class.java.getResource("puzzle1.txt").readText().split('\n').filter { it.isNotEmpty() }.map { it.toInt() }
                val result = input.fold(0) {acc, mass ->
                        val fuel = calculateFuel(mass)
                        acc + fuel }
                println(result)

                val recursiveResult = input.fold(0) {acc, mass ->
                        val fuel = calculateFuelRecursive(mass)
                        acc + fuel }
                println(recursiveResult)
        }
}