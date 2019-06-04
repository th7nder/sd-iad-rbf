package network.centers.selforganizing.common

import math.Point

class Centroid(point: Point) : Point(point) {
    val points = ArrayList<Point>()
    fun add(p: Point) = points.add(p)
    fun clear() = points.clear()

    fun sumDistanceToPoints() : Double = points.sumByDouble { it.distance(this) }
}