package network.centers

import math.Point
import network.centers.selforganizing.common.Centroid
import network.centers.selforganizing.kaverage.KAverage
import network.centers.selforganizing.neuralgas.DistanceOrder
import network.centers.selforganizing.neuralgas.NeuralGas

class KAverageGenerator(
    private val numIterations: Int
) : FromDataGenerator() {

    override fun generate(numCenters: Int, data: List<Point>): List<Point> {
        val centroids = super.generate(numCenters, data).map { Centroid(it) }
        val kAverage = KAverage(data, centroids)

        kAverage.train(numIterations) { iteration, error ->
            println("$kAverage | $iteration | $error ")
        }

        return centroids
    }
}