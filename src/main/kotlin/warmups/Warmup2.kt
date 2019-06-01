package warmups

import files.DataLoader
import math.Utils
import math.input
import network.Network
import network.centers.FromDataGenerator
import network.sigmas.EqualSigmaGenerator
import utils.Charts

class Warmup2 : Warmup() {
    private val data = DataLoader.loadFile("approx1", 1, 1)
    private val arguments : List<Double>

    init {
        val from = data.input().minBy { it.x() }!!.x()
        val to = data.input().maxBy { it.x() }!!.x()
        arguments = Utils.arange(from, to, 0.01)
    }

    fun taskK4() {
        val network = Network(
            4,
            1,
            data.input(),
            FromDataGenerator(),
            EqualSigmaGenerator()
        )

        Charts.saveChart("out4_approx", plotNetwork(arguments, network))
    }

    fun taskK10() {
        val network = Network(
            10,
            1,
            data.input(),
            FromDataGenerator(),
            EqualSigmaGenerator()
        )

        Charts.saveChart("out10_approx", plotNetwork(arguments, network))
    }
}

fun main() {
    val warmup = Warmup2()
    warmup.taskK4()
    warmup.taskK10()
}