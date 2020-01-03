fun String.toOrbitMapByChildrenList(): HashMap<String, MutableList<String>> {
    val map = HashMap<String, MutableList<String>>()
    val lines = this.split("\n")
    lines.forEach {
        val objects = it.split(")")
        val center = objects.first()
        val otherObject = objects.last()
        if (!map.containsKey(center)) {
            map[center] = ArrayList()
        }
        map[center]?.add(otherObject)
    }
    return map
}

fun String.toOrbitMapByParent(): HashMap<String, String> {
    val map = HashMap<String, String>()
    val lines = this.split("\n")
    lines.forEach {
        val objects = it.split(")")
        val center = objects.first()
        val otherObject = objects.last()
        map[otherObject] = center
    }
    return map
}

object Puzzle6 {


    @JvmStatic
    fun main(args : Array<String>) {
        val input =
            Puzzle6::class.java.getResource("puzzle6.txt").readText().trim()

        println(sumOrbits(input.toOrbitMapByChildrenList()))


        val map = input.toOrbitMapByParent()
        val routeFromF = getRouteToCom("YOU", map)
        val routeFromL = getRouteToCom("SAN", map)
        val minDistance = getMinimumDistanceOnRoutes(routeFromF, routeFromL)

        println(minDistance)
    }

    fun sumOrbits(orbitMap: HashMap<String, MutableList<String>>): Int {
        var sum = 0
        var nextOrbits = arrayOf("COM")
        var distance = 0
        do {
            sum += nextOrbits.size * distance
            distance ++
            nextOrbits = orbitMap.filter { nextOrbits.contains(it.key) }.map { it.value }.flatten().toTypedArray()
        }while (nextOrbits.isNotEmpty())



        return sum

    }

    fun getRouteToCom(from: String, map: HashMap<String, String>): HashMap<String, Int>{
        val route = HashMap<String, Int>()
        var distance = 0
        var location = from
        while (map.containsKey(location)){
            location = map[location]!!
            route[location] = distance
            distance++
        }
        return route
    }

    fun getMinimumDistanceOnRoutes(route1: HashMap<String, Int>, route2: HashMap<String, Int>): Int {
        val summedRoute = HashMap<String, Int>()
        route1.forEach{(location, distance) ->
            if (route2.containsKey(location)){
                summedRoute[location] = route1[location]!! + route2[location]!!
            }
        }
        return summedRoute.minBy { it.value }!!.value
    }
}