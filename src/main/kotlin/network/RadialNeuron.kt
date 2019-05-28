package network

import math.Point
import kotlin.math.pow

class RadialNeuron(dimension : Int) {
    var sigma : Double = 0.0
    var center : Point = Point(dimension)

    constructor(center : Point, sigma : Double) : this(center.dimension()){
        this.center.setLocation(center)
        this.sigma = sigma
    }

    fun output(x : Point) = R(center.distance(x), sigma)

    fun R(d: Double, sigma: Double) = Math.exp(
        -d.pow(2) / (2 * sigma.pow(2))
    )
}