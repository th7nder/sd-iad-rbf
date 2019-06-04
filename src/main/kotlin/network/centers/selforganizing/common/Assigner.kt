package network.centers.selforganizing.common

import math.Point

class Assigner {
    fun assign(data: List<Point>, centroids: List<Centroid>) {
        for (point in data) {
            val centroid = centroids.minBy { it.distance(point) }
            centroid?.add(point)
        }
    }
}