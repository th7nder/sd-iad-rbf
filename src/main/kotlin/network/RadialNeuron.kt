package network

import math.Point
import kotlin.math.pow
import kotlin.random.Random

class RadialNeuron(dimension : Int, sigma : Double = 1.0) {
    private var sigma : Double = sigma
    private val center : Point = Point(dimension)

    init {
        for (index in center.coordinates.indices) {
            center.coordinates[index] = Random.nextDouble(0.0, 10.0)
        }
    }

    fun output(x : Point) = R(center.distance(x))

    fun R(d: Double) = Math.exp(
        -d.pow(2) / (2 * sigma.pow(2))
    )
}