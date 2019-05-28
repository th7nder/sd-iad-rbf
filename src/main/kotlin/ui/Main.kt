package ui

import math.Point
import math.Utils
import network.Network
import utils.Charts

fun main() {
    val network = Network(1, 4, 1)

    val arguments = Utils.arange(0.0, 10.0, 0.01)
    val values = arguments.map { network.output(Point(it)).first() }.toList()

    Charts.saveChart("out4.png", Charts.createChart(arguments, values, "Sieć RBF"))
}