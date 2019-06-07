package tasks.classification

import files.DataLoader
import math.Point
import math.Utils
import network.Network
import network.centers.CenterGenerator
import network.centers.KAverageGenerator
import network.sigmas.EqualSigmaGenerator
import network.sigmas.SigmaGenerator
import java.util.*

typealias DataSet = ArrayList<Pair<Point, Point>>
typealias Combination = Pair<Int, List<Int>>

open class Classification {
    val trainingData = DataLoader.loadFile("classification_train", 4, ::intToPointParser)
    val testData = DataLoader.loadFile("classification_test", 4, ::intToPointParser)
    val alpha = 0.05
    val kAverageIterations = 10
    var derivatives = false

    val combinations = arrayListOf(
        Pair(1, listOf(1)),
        Pair(1, listOf(2)),
        Pair(1, listOf(3)),
        Pair(1, listOf(4)),
        Pair(2, listOf(2, 3)),
        Pair(2, listOf(3, 4)),
        Pair(2, listOf(1, 2)),
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
    

    fun percentage(data: DataSet, network: Network) : Double {
        var classified = 0
        for (pair in data) {
            val output = network.output(pair.first).coordinates
            val expected = pair.second.coordinates
            val maxIndex = Utils.argmax(output)

            if (expected[maxIndex].toInt() == 1) {
                classified += 1
            }
        }

        return classified / data.size.toDouble()
    }

    fun createNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator, centerGenerator: CenterGenerator, dataSet: DataSet) = Network(numRadialNeurons, 3, dataSet, centerGenerator, sigmaGenerator, alpha, derivatives)
    fun createNetwork(numRadialNeurons: Int, trainingDataSet: DataSet) = createNetwork(numRadialNeurons, EqualSigmaGenerator(), KAverageGenerator(kAverageIterations), trainingDataSet)

    fun singleNetwork(numRadialNeurons: Int, trainingDataSet: DataSet = trainingData) : Network {
        val network = createNetwork(numRadialNeurons, trainingDataSet)
        network.train(getTrainingIterations()) {
                i, error ->
            if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error | classify: ${percentage(trainingDataSet, network)}")
        }

        return network
    }


    open fun getTrainingIterations(): Int = 5000
    open fun getDisplayIterations(): Int = 1000
}

fun main() {
    val classification = Classification()
    classification.singleNetwork(
        20
    )
}