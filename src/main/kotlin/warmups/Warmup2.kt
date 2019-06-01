package warmups

import files.DataLoader
import math.Utils
import math.input
import math.output
import math.project
import network.Network
import network.centers.FromDataGenerator
import network.sigmas.EqualSigmaGenerator
import org.knowm.xchart.XYChart
import org.knowm.xchart.style.lines.SeriesLines
import org.knowm.xchart.style.markers.SeriesMarkers
import utils.Charts
import java.awt.Color

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
            data,
            FromDataGenerator(),
            EqualSigmaGenerator(),
            0.5
        )

        network.train(6000) { i, error -> if (i % 1000 == 0) println("$i $error") }

        Charts.saveChart("out4_approx", plotNetwork(arguments, network))
    }

    fun taskK10() {
        val network = Network(
            10,
            1,
            data,
            FromDataGenerator(),
            EqualSigmaGenerator(),
            0.6
        )

        network.train(6000) { i, error -> if (i % 1000 == 0) println("$i $error") }
        println("Trained...")

        Charts.saveChart("out10_approx", plotNetwork(arguments, network))
    }

    override fun plotNetwork(arguments: List<Double>, network: Network): XYChart {
        val chart = super.plotNetwork(arguments, network)

        val series = chart.addSeries("Punkty treningowe", data.input().project(0), data.output().project(0))
        series.lineStyle = SeriesLines.NONE
        series.markerColor = Color.GREEN
        series.marker = SeriesMarkers.CIRCLE

        return chart
    }
}

fun main() {
    val warmup = Warmup2()
    warmup.taskK4()
    warmup.taskK10()
}