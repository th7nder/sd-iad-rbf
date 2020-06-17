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
    val trainingData = DataLoader.loadCSV("new/1.csv", 3, 2)

    val network = NetworkV2()
    val inputLayer = Layer(
        (1..30).map { SigmoidNeuron(3) }
    )
    val outputLayer = Layer(
        listOf(
            IdentityNeuron(30),
            IdentityNeuron(30)
        )
    )
    network.layers.add(inputLayer)
    network.layers.add(outputLayer)


    for (iteration in 1..10000) {
        println("$iteration ${network.error(trainingData)}")
        for (dataPoint in trainingData) {
            network.output(dataPoint.first, dataPoint.second)
        }
        for (neuron in inputLayer.neurons) {
            for (i in neuron.weights.indices) {
                neuron.weights[i] += neuron.updates[i] / trainingData.size
                neuron.updates[i] = 0.0
            }
        }
        for (neuron in outputLayer.neurons) {
            for (i in neuron.weights.indices) {
                neuron.weights[i] += neuron.updates[i] / trainingData.size
                neuron.updates[i] = 0.0
            }
        }
    }
}