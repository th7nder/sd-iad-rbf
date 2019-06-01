package tasks.Approximation3

import files.DataLoader
import math.*
import network.Network
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.lines.SeriesLines
import org.knowm.xchart.style.markers.SeriesMarkers

abstract class Approximation3 {
    val trainingData = DataLoader.loadFile("approx_train", 1, 1)
    val testData = DataLoader.loadFile("approx1", 1, 1)
    val arguments : List<Double>

    init {
        val from = trainingData.input().minBy { it.x() }!!.x()
        val to = trainingData.input().maxBy { it.x() }!!.x()
        arguments = Utils.arange(from, to, 0.01)
    }

    open fun plotNetwork(title: String, networks: List<Network>) : XYChart {
        val networkOutputs = networks.map { network -> Pair(network, arguments.map { network.output(Point(it)) }) }

        return createChart(
            title,
            networkOutputs
        )
    }

    fun createChart(
        title: String,
        networkOutputs: List<Pair<Network, List<Point>>>
    ): XYChart {
        val chart =
            XYChartBuilder()
                .width(800)
                .height(800)
                .title(title).build()



        val trening = chart.addSeries("Dane treningowe", trainingData.input().project(0), trainingData.output().project(0))
        trening.lineStyle = SeriesLines.NONE
        trening.marker = SeriesMarkers.CIRCLE

        val test = chart.addSeries("Dane testowe", testData.input().project(0), testData.output().project(0))
        test.lineStyle = SeriesLines.NONE
        test.marker = SeriesMarkers.CIRCLE

        networkOutputs.forEach { (network, outputs) ->
            val series = chart.addSeries("K=${network.numRadialNeurons}", arguments, outputs.project(0))
            series.marker = SeriesMarkers.NONE
        }

        return chart
    }
}