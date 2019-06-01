package warmups

import math.Point
import math.Utils
import network.Network
import org.knowm.xchart.XYChart
import utils.Charts

class Warmup1 : Warmup() {
    fun taskK4() {
        val network = Network(
            1,
            arrayListOf(4.0, 3.0, 0.6, 0.5),
            arrayListOf(
                Point(1.0),
                Point(2.0),
                Point(5.0),
                Point(8.0)
            ),
            1
        )


        Charts.saveChart("out4", plotNetwork(network))
    }

    fun taskK10() {
        val network = Network(
            1,
            arrayListOf(0.5, .7, 1.3, 3.0, 3.5, 4.5, 0.3, 0.4, 2.0, 2.0),
            arrayListOf(
                Point(1.0),
                Point(1.4),
                Point(2.0),

                Point(3.0),
                Point(4.0),
                Point(5.0),

                Point(6.0),
                Point(6.5),

                Point(8.0),
                Point(9.0)
            ),
            1
        )


        Charts.saveChart("out10", plotNetwork(network))
    }
}