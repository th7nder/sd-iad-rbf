package tasks.Approximation3

import math.project
import network.sigmas.EqualSigmaGenerator
import org.knowm.xchart.style.markers.SeriesMarkers
import utils.Charts

class XSteps4 : Approximation3() {
    val folder = "approximation3/4"
    val numSubsteps = 6
    val numRadialNeurons = 16
    val iterationsPerStep = getTrainingIterations() / (numSubsteps - 1)
    val subSteps = (0..getTrainingIterations() step iterationsPerStep)

    override fun getTrainingIterations() = 5000

    fun run() {
        val network = createNetwork(numRadialNeurons, EqualSigmaGenerator())
        val chart = plotNetwork("Sieć RBF - K=$numRadialNeurons", arrayListOf())
        chart.styler.yAxisMin = -8.0
        chart.styler.yAxisMax = 3.0

        for (step in subSteps) {
            if (step != 0) network.train (iterationsPerStep)
            chart
                .addSeries("i=$step", arguments, mapNetworkToOutputs(network).second.project(0))
                .marker = SeriesMarkers.NONE
            println("After $step")
        }

        Charts.saveChart("$folder/chart", chart)
    }
}

fun main() {
    val xSteps4 = XSteps4().apply {
        run()
    }
}