package math

import kotlin.math.pow

class Point(n: Int) : Cloneable {
    val coordinates = DoubleArray(n)

    public override fun clone(): Point {
        val point = Point(coordinates.size)
        point.setLocation(this)
        return point
    }

    constructor(vararg coordinates : Double) : this(coordinates.size) {
        for (index in coordinates.indices) {
            this.coordinates[index] = coordinates[index]
        }
    }

    fun setLocation(vararg coordinates: Double) {
        for (index in coordinates.indices) {
            this.coordinates[index] = coordinates[index]
        }
    }

    fun setLocation(b: Point) {
        for (index in b.coordinates.indices) {
            this.coordinates[index] = b.coordinates[index]
        }
    }

    fun dimension() = coordinates.size

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