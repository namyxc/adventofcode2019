object Puzzle4 {
    fun checknumber(number: Int): Boolean{
        val numbers = number.toString().map { it.toString().toInt() }
        var hasDouble = false
        for (i in 1..numbers.size-1){
            if (numbers[i-1]>numbers[i]){
                return false
            }
            if (numbers[i-1] == numbers[i])
                hasDouble = true
        }
        return hasDouble
    }

    fun checknumberNoTriplets(number: Int): Boolean{
        val numbers = number.toString().map { it.toString().toInt() }
        for (i in 1..numbers.size-1){
            if (numbers[i-1]>numbers[i]){
                return false
            }
        }
        return numbers.groupingBy {it}.eachCount().containsValue(2)
    }


    @JvmStatic
    fun main(args : Array<String>) {
        println((125730..579381).count(::checknumber))
        println((125730..579381).count(::checknumberNoTriplets))
    }
}