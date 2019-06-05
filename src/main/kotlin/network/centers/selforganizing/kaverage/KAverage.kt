package network.centers.selforganizing.kaverage

import math.Point
import math.project
import network.centers.selforganizing.Algorithm
import network.centers.selforganizing.common.Centroid

class KAverage() : Algorithm() {

    constructor(data: List<Point>, centroids: List<Centroid>) : this() {
        setTrainingData(data, centroids)
    }

    private fun update() {
        for (centroid in centroids) {
            if (centroid.points.size == 0) continue

            val coordinates = DoubleArray(centroid.dimension())
            for (coordinate in coordinates.indices) {
                coordinates[coordinate] = centroid.points.project(coordinate).average()
            }

            centroid.update(coordinates)
        }
    }

    override fun step(iteration: Int) {
        update()
        assign()
    }

    override fun toString(): String = "K-Średnich"
}