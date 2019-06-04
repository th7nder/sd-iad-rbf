package tasks.approximation

import files.DataLoader
import math.*
import network.Network
import network.centers.FromDataGenerator
import network.sigmas.SigmaGenerator
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.lines.SeriesLines
import org.knowm.xchart.style.markers.SeriesMarkers

abstract class Approximation3 {
    val trainingData = DataLoader.loadFile("approx1", 1, 1)
    val testData = DataLoader.loadFile("approx_test", 1, 1)
    val arguments : List<Double>
    val alpha = 0.05

    init {
        val from = trainingData.input().minBy { it.x() }!!.x()
        val to = trainingData.input().maxBy { it.x() }!!.x()
        arguments = Utils.arange(from, to, 0.01)
    }


    fun mapNetworkToOutputs(network: Network): Pair<Network, List<Point>> = Pair(network, arguments.map { network.output(Point(it)) })

    open fun plotNetwork(title: String, networks: List<Network>) : XYChart {
        val networkOutputs = networks.map(::mapNetworkToOutputs)

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

        val test = chart.addSeries("Dane testowe", testData.input().project(0), testData.output().project(0))
        test.lineStyle = SeriesLines.NONE
        test.marker = SeriesMarkers.CIRCLE

        val trening = chart.addSeries("Dane treningowe", trainingData.input().project(0), trainingData.output().project(0))
        trening.lineStyle = SeriesLines.NONE
        trening.marker = SeriesMarkers.CIRCLE

        networkOutputs.forEach { (network, outputs) ->
            val series = chart.addSeries("K=${network.numRadialNeurons}", arguments, outputs.project(0))
            series.marker = SeriesMarkers.NONE
        }

        return chart
    }


    fun createNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator) = Network(numRadialNeurons, 1, trainingData, FromDataGenerator(), sigmaGenerator, alpha)

    fun singleNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator) : Network {
        val network = createNetwork(numRadialNeurons, sigmaGenerator)
        network.train(getTrainingIterations()) { i, error -> if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error")}

        return network
    }

    open fun getDisplayIterations() = 5000
    open fun getTrainingIterations() = 20000
}