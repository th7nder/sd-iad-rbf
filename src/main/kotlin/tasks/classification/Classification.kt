package tasks.classification

import files.DataLoader
import math.Point
import network.Network
import network.centers.FromDataGenerator
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

    fun createNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator) = Network(numRadialNeurons, 3, trainingData, FromDataGenerator(), sigmaGenerator, alpha)

    fun singleNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator) : Network {
        val network = createNetwork(numRadialNeurons, sigmaGenerator)
        network.train(getTrainingIterations()) { i, error -> if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error")}

        return network
    }

    open fun getDisplayIterations() = 1
    open fun getTrainingIterations() = 1000
}

fun main() {
    val classification = Classification()
    classification.singleNetwork(10, EqualSigmaGenerator())
}