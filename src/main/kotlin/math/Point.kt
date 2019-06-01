package math

import kotlin.math.pow

fun Collection<Pair<Point, Point>>.input() = map { it.first }
fun Collection<Pair<Point, Point>>.output() = map { it.second }

class Point(n: Int) : Cloneable {
    val coordinates = DoubleArray(n)

    fun x() = coordinates[0]

    public override fun clone(): Point {
        val point = Point(coordinates.size)
        point.setLocation(this)
        return point
    }

    constructor(coordinates: List<Double>) : this(coordinates.size){
        for ((index, coordinate) in coordinates.withIndex()) {
            this.coordinates[index] = coordinate
        }
    }

    constructor(coordinates : DoubleArray) : this(coordinates.size) {
        setLocation(coordinates)
    }

    constructor(x: Double) : this(1) {
        coordinates[0] = x
    }

    fun setLocation(coordinates: DoubleArray) {
        for ((index, coordinate) in coordinates.withIndex()) {
            this.coordinates[index] = coordinate
        }
    }

    fun setLocation(b: Point) {
        setLocation(b.coordinates)
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