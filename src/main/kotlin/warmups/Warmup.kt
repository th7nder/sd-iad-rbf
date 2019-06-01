package warmups

import math.Point
import math.Utils
import network.Network
import org.knowm.xchart.XYChart
import utils.Charts

abstract class Warmup {
    protected fun plotNetwork(arguments: List<Double>, network: Network) : XYChart {

        var values = arguments.map { network.output(Point(it)).first() }.toList()

        val partials = ArrayList<Pair<List<Double>, List<Double>>>()
        val outputNeuron = network.outputLayer.first()
        for (idx in 1..network.numRadialNeurons) {
            partials.add(
                Pair(
                    arguments,
                    arguments.map { outputNeuron.applyWeights(Point(it))[idx] }.toList()
                )
            )
        }

        return Charts.createFirstWarmupChart(
            Pair(arguments, values),
            partials,
            "Sieć RBF"
        )
    }
}