package tasks.track

import files.DataLoader
import math.Point
import math.input
import math.output
import math.project
import network.Layer
import network.Network
import network.NetworkV2
import network.centers.CenterGenerator
import network.centers.KAverageGenerator
import network.neurons.IdentityNeuron
import network.neurons.SigmoidNeuron
import network.sigmas.EqualSigmaGenerator
import network.sigmas.SigmaGenerator
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.lines.SeriesLines
import org.knowm.xchart.style.markers.SeriesMarkers
import utils.Charts



fun main() {
    val trainingData = DataLoader.loadCSV("new/1.csv", 3, 2)

    val network = NetworkV2()
    val inputLayer = Layer(
        (1..10).map { SigmoidNeuron(3) }
    )
    val outputLayer = Layer(
        listOf(
            IdentityNeuron(10),
            IdentityNeuron(10)
        )
    )
    network.layers.add(inputLayer)
    network.layers.add(outputLayer)

    val test = network.output(
        Point(doubleArrayOf(1.0, 1.0, 1.0)),
        Point(doubleArrayOf(1.0, 1.0))
    )
}