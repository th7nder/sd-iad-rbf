package network

import math.Point
import kotlin.math.pow

class RadialNeuron(private val center : Point, private val sigma : Double) {
    fun output(x : Point) = R(center.distance(x))

    fun R(d: Double) = Math.exp(
        -d.pow(2) / (2 * sigma.pow(2))
    )
}