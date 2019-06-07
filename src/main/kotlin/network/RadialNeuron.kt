package network

import math.Point
import kotlin.math.pow

class RadialNeuron(private val center : Point, private var sigma : Double) {
    fun z(x : Point) = radialFunction(center.distance(x))

    fun radialFunction(d: Double) = Math.exp(
        -d.pow(2) / (2 * sigma.pow(2))
    )

    fun step(differences: List<Pair<Point, Double>>, alpha: Double, weight: Double) {
        var sigmaAdjustment = 0.0

        val partials = differences.map { (input, difference) -> Pair(input, difference * z(input) * weight) }
        for ((input, partial) in partials) {
            sigmaAdjustment += partial * center.distance(input).pow(2) / sigma.pow(3)
        }

        sigmaAdjustment /= differences.size

        val coordinateAdjustments = DoubleArray(center.dimension())
        for ((input, partial) in partials) {
            val sigmed = partial / sigma.pow(2)

            for (index in coordinateAdjustments.indices) {
                coordinateAdjustments[index] += sigmed * (input.coordinates[index] - center.coordinates[index])
            }
        }

        for (index in coordinateAdjustments.indices) {
            center.coordinates[index] -= alpha * (coordinateAdjustments[index] / partials.size)
        }

        sigma -= alpha * sigmaAdjustment
    }
}