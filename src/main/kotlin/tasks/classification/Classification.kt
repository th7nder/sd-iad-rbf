package tasks.classification

import files.DataLoader
import math.Point
import math.Utils
import network.Network
import network.centers.CenterGenerator
import network.centers.FromDataGenerator
import network.centers.NeuralGasGenerator
import network.sigmas.EqualSigmaGenerator
import network.sigmas.SigmaGenerator
import java.util.*

typealias DataSet = ArrayList<Pair<Point, Point>>

class Classification {
    val trainingData = DataLoader.loadFile("classification_train", 4, ::intToPointParser)
    val alpha = 0.01

    val combinations = arrayListOf(
        Pair(1, listOf(1)),
        Pair(1, listOf(2)),
        Pair(1, listOf(3)),
        Pair(1, listOf(4)),
        Pair(2, listOf(2, 3)),
        Pair(2, listOf(3, 4)),
        Pair(2, listOf(1, 3)),
        Pair(2, listOf(1, 4)),
        Pair(2, listOf(2, 4)),
        Pair(3, listOf(1, 2, 3)),
        Pair(3, listOf(1, 2, 4)),
        Pair(3, listOf(1, 3, 4)),
        Pair(3, listOf(2, 3, 4)),
        Pair(4, listOf(1, 2, 3, 4))
    )

    fun projectData(data: DataSet, combination: Pair<Int, List<Int>>) : DataSet {
        val projectedData = DataSet()
        for (pair in data) {
            val input = pair.first
            val output = pair.second
            val coordinates = DoubleArray(combination.first)

            for ( (selected, index) in combination.second zip (0 until combination.first)) {
                coordinates[index] = input.coordinates[selected - 1]
            }

            projectedData.add(
                Pair(
                    Point(coordinates),
                    output
                )
            )
        }

        return projectedData
    }

    private fun intToPointParser(value : Int) : Point {
        val coordinates = mutableListOf(1.0, 0.0, 0.0)
        Collections.rotate(coordinates, value - 1)
        return Point(coordinates)
    }

    private fun percentage(data: DataSet, network: Network) : Double {
        var classified = 0
        for (pair in data) {
            val output = network.output(pair.first).coordinates
            val expected = pair.second.coordinates
            val maxIndex = Utils.argmax(output)

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
            if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error | classify: ${percentage(trainingData, network)}")
        }

        return network
    }


    open fun getDisplayIterations() = 100
    open fun getTrainingIterations() = 10000
}

fun main() {
    val classification = Classification()
    //val x = classification.projectData(classification.trainingData, classification.combinations[4])
    classification.singleNetwork(
        10,
        EqualSigmaGenerator(),
        NeuralGasGenerator(10, 0.1, 1.0, 0.1)
    )
}