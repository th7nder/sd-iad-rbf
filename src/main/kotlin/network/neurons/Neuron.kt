package network.neurons

import math.Point
import kotlin.random.Random

abstract class Neuron(numInputs: Int) {
    var bias = Random.nextDouble(-4.0, 4.0)
    val weights = DoubleArray(numInputs)
    val updates = DoubleArray(numInputs)

    init {
        for (i in weights.indices) {
            weights[i] = Random.nextDouble(-1.0, 1.0)
        }
    }

    abstract fun f(x : Double) : Double
    abstract fun derivative(x : Double) : Double

    fun output(x: Point): Double {
        return f(a(x))
    }

    fun a(x: Point): Double {
        return (x.coordinates zip weights).map { it.first * it.second }.sum()
    }
}