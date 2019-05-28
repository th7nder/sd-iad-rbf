package math

import kotlin.math.pow

class Point(vararg val coordinates : Double) {
    fun distance(b: Point) : Double {
        if (coordinates.size != b.coordinates.size) {
            throw IllegalArgumentException("calculate point in different dimensions")
        }

        return Math.sqrt(
            (coordinates zip b.coordinates)
            .map { (it.first - it.second).pow(2) }
            .sum()
        )
    }
}