package network

import math.Point
import kotlin.math.pow

class RadialNeuron(private val center : Point, private val sigma : Double) {
    fun output(x : Point) = radialFunction(center.distance(x))

    fun radialFunction(d: Double) = Math.exp(
        -d.pow(2) / (2 * sigma.pow(2))
    )
}