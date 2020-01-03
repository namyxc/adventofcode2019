import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test


internal class Puzzle6Test {


    /*
            G - H       J - K - L
           /           /
    COM - B - C - D - E - F
                   \
                    I
     */

    val input = "COM)B\n" +
            "B)C\n" +
            "C)D\n" +
            "D)E\n" +
            "E)F\n" +
            "B)G\n" +
            "G)H\n" +
            "D)I\n" +
            "E)J\n" +
            "J)K\n" +
            "K)L"

    @Test
    fun toOrbitMapByChildrenList() {
        val output = input.toOrbitMapByChildrenList()

        assertThat(output["COM"], containsInAnyOrder("B"))
        assertThat(output["B"], containsInAnyOrder("C", "G"))
        assertThat(output["G"], containsInAnyOrder("H"))
        assertThat(output["C"], containsInAnyOrder("D"))
        assertThat(output["D"], containsInAnyOrder("E", "I"))
        assertThat(output["E"], containsInAnyOrder("J", "F"))
        assertThat(output["J"], containsInAnyOrder("K"))
        assertThat(output["K"], containsInAnyOrder("L"))
    }

    @Test
    fun toOrbitMapByParent() {
        val output = input.toOrbitMapByParent()
        assertThat(output["B"], equalTo("COM"))
        assertThat(output["C"], equalTo("B"))
        assertThat(output["G"], equalTo("B"))
        assertThat(output["H"], equalTo("G"))
        assertThat(output["D"], equalTo("C"))
        assertThat(output["E"], equalTo("D"))
        assertThat(output["I"], equalTo("D"))
        assertThat(output["J"], equalTo("E"))
        assertThat(output["F"], equalTo("E"))
        assertThat(output["K"], equalTo("J"))
        assertThat(output["L"], equalTo("K"))
    }

    @Test
    fun sumOrbits() {
        val sum = Puzzle6.sumOrbits(input.toOrbitMapByChildrenList())
        assertThat(42, equalTo(sum))
    }

    @Test
    fun getRouteToCom() {
        val routes = Puzzle6.getRouteToCom("L", input.toOrbitMapByParent())

        assertThat(routes["K"], equalTo(1))
        assertThat(routes["J"], equalTo(2))
        assertThat(routes["E"], equalTo(3))
        assertThat(routes["D"], equalTo(4))
        assertThat(routes["C"], equalTo(5))
        assertThat(routes["B"], equalTo(6))
        assertThat(routes["COM"], equalTo(7))
    }

    @Test
    fun getMinimumDistanceOnRoutes() {
        val inputWithSANandYOU = "COM)B\n" +
                "B)C\n" +
                "C)D\n" +
                "D)E\n" +
                "E)F\n" +
                "B)G\n" +
                "G)H\n" +
                "D)I\n" +
                "E)J\n" +
                "J)K\n" +
                "K)L\n" +
                "K)YOU\n" +
                "I)SAN"

        /*
                                          YOU
                                         /
                        G - H       J - K - L
                       /           /
                COM - B - C - D - E - F
                               \
                                I - SAN
        * */

        val map = inputWithSANandYOU.toOrbitMapByParent()
        val routeFromYOU = Puzzle6.getRouteToCom("YOU", map)
        val routeFromSAN = Puzzle6.getRouteToCom("SAN", map)
        val minDistance = Puzzle6.getMinimumDistanceOnRoutes(routeFromYOU, routeFromSAN)

        assertThat(minDistance, equalTo(4))
    }

}