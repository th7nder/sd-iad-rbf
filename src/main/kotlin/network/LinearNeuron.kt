package network

import math.Point
import kotlin.random.Random

class LinearNeuron(dimension: Int, numPreviousNeurons: Int) {
    private val weights = DoubleArray(numPreviousNeurons + 1)
    private val neurons = arrayListOf(RadialNeuron(dimension, 0.0))

    init {
        for (index in weights.indices) {
            weights[index] = Random.nextDouble(-4.0, 4.0)
        }
    }

    fun connect(neurons : List<RadialNeuron>) {
        if (this.neurons.size + neurons.size > weights.size) {
            throw IllegalStateException("add more neurons than it can operate on")
        }

        this.neurons.addAll(neurons)
    }

    fun output(x: Point) : Double {
        if (weights.size != neurons.size) {
            throw IllegalStateException("calculate without proper connected neuron number")
        }

        return neurons.mapIndexed { index, neuron ->  weights[index] * neuron.output(x) }.sum()
    }
}