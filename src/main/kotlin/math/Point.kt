package math

import kotlin.math.pow

fun Collection<Pair<Point, Point>>.input() = map { it.first }
fun Collection<Pair<Point, Point>>.output() = map { it.second }
fun Collection<Point>.project(n: Int) = map {it.coordinates[n]}

open class Point(n: Int) : Cloneable {
    val coordinates = DoubleArray(n)

    fun x() = coordinates[0]

    fun y() = coordinates[1]

    public override fun clone(): Point {
        val point = Point(coordinates.size)
        point.update(this)
        return point
    }

    constructor(coordinates: List<Double>) : this(coordinates.size){
        for ((index, coordinate) in coordinates.withIndex()) {
            this.coordinates[index] = coordinate
        }
    }

    constructor(coordinates : DoubleArray) : this(coordinates.size) {
        update(coordinates)
    }

    constructor(point: Point) : this(point.dimension()){
        update(point)
    }

    constructor(x: Double) : this(1) {
        coordinates[0] = x
    }

    fun update(coordinates: DoubleArray) {
        for ((index, coordinate) in coordinates.withIndex()) {
            this.coordinates[index] = coordinate
        }
    }

    fun update(b: Point) {
        update(b.coordinates)
    }

    operator fun minus(b: Point) : Point {
        if (coordinates.size != b.coordinates.size) {
            throw IllegalArgumentException("calculate point in different dimensions")
        }

        val result = clone()
        for (index in b.coordinates.indices) {
            result.coordinates[index] -= b.coordinates[index]
        }

        return result
    }

    operator fun plus(b: Point) : Point {
        if (coordinates.size != b.coordinates.size) {
            throw IllegalArgumentException("calculate point in different dimensions")
        }

        val result = clone()
        for (index in b.coordinates.indices) {
            result.coordinates[index] += b.coordinates[index]
        }

        return result
    }

    operator fun times(b: Double) : Point {
        val result = clone()
        for (index in coordinates.indices) {
            result.coordinates[index] *= b
        }

        return result
    }


    fun pow(x: Double) : Point {
        val result = clone()

        for (index in coordinates.indices) {
            result.coordinates[index] = coordinates[index].pow(x)
        }

        return result
    }

    fun sum() : Double = coordinates.sum()

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