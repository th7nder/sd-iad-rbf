package tasks.classification

import files.DataLoader
import math.Point
import network.Network
import network.centers.CenterGenerator
import network.centers.FromDataGenerator
import network.centers.NeuralGasGenerator
import network.sigmas.EqualSigmaGenerator
import network.sigmas.SigmaGenerator
import java.util.*

class Classification {
    val trainingData = DataLoader.loadFile("classification_train", 4, ::intToPointParser)
    val alpha = 0.01

    private fun intToPointParser(value : Int) : Point {
        val coordinates = mutableListOf(1.0, 0.0, 0.0)
        Collections.rotate(coordinates, value - 1)
        return Point(coordinates)
    }

    private fun argmax(arr: DoubleArray) : Int {
        var maxIndex = 0
        for (index in arr.indices) {
            if (arr[index] > arr[maxIndex]) {
                maxIndex = index
            }
        }

        return maxIndex
    }

    private fun percentage(network: Network) : Double {
        var classified = 0
        for (data in trainingData) {
            val output = network.output(data.first).coordinates
            val expected = data.second.coordinates
            val maxIndex = argmax(output)

            if (expected[maxIndex].toInt() == 1) {
                classified += 1
            }
        }

        return classified / trainingData.size.toDouble()
    }

    fun createNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator, centerGenerator: CenterGenerator) = Network(numRadialNeurons, 3, trainingData, centerGenerator, sigmaGenerator, alpha)

    fun singleNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator, centerGenerator: CenterGenerator) : Network {
        val network = createNetwork(numRadialNeurons, sigmaGenerator, centerGenerator)
        network.train(getTrainingIterations()) {
                i, error ->
            if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error | classify: ${percentage(network)}")
        }

        return network
    }

    open fun getDisplayIterations() = 100
    open fun getTrainingIterations() = 5000
}

fun main() {
    val classification = Classification()
    classification.singleNetwork(10, EqualSigmaGenerator(), NeuralGasGenerator(10, 0.1, 2.5, 0.5))
}