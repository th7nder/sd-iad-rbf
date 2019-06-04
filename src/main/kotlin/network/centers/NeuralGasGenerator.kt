package network.centers

import math.Point
import network.centers.selforganizing.common.Centroid
import network.centers.selforganizing.neuralgas.DistanceOrder
import network.centers.selforganizing.neuralgas.NeuralGas

class NeuralGasGenerator(
    private val numIterations: Int,
    private val alpha : Double,
    private val lambda: Double,
    private val lambdaDrop: Double
) : FromDataGenerator() {

    override fun generate(numCenters: Int, data: List<Point>): List<Point> {
        val centroids = super.generate(numCenters, data).map { Centroid(it) }
        val neuralGas = NeuralGas(
            data,
            centroids,
            DistanceOrder { Math.max(lambda - it * lambdaDrop, 0.01) }
        ) {alpha}

        neuralGas.train(numIterations) { iteration, error ->
            println("Neural gas | $iteration | $error ")
        }

        return centroids
    }
}