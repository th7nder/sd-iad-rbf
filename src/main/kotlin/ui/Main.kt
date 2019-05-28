package ui

import math.Point
import math.Utils
import network.Network
import utils.Charts

fun main() {
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

    val arguments = Utils.arange(0.0, 10.0, 0.01)
    var values = arguments.map { network.output(Point(it)).first() }.toList()

    val partials = ArrayList<Pair<List<Double>, List<Double>>>()
    for (radialNeuron in network.radialLayer) {
        partials.add(
            Pair(
                arguments,
                arguments.map {radialNeuron.output(Point(it))}.toList()
            )
        )
    }

    Charts.saveChart(
        "out4.png",
        Charts.createFirstWarmupChart(
            Pair(arguments, values),
            partials,
            "Sieć RBF"
        )
    )
}