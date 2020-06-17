package tasks.track

import files.DataLoader
import math.Point
import math.input
import math.output
import math.project
import network.Layer
import network.LinearNeuron
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
    val trainingData = DataLoader.loadFile("approx1", 1, 1)

    val network = NetworkV2()
    val inputLayer = Layer(
        (1..10).map { SigmoidNeuron(1) }
    )
    val middleLayer = Layer(
        (1..3).map { SigmoidNeuron(10) }
    )
    val outputLayer = Layer(
        listOf(
            IdentityNeuron(3)
        )
    )
    network.layers.add(inputLayer)
    network.layers.add(middleLayer)
    network.layers.add(outputLayer)


    for (iteration in 1..30000) {
        println("$iteration ${network.error(trainingData)}")
        network.train(trainingData)
    }
}