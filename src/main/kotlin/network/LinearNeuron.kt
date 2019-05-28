package network

import math.Point

class LinearNeuron(numNeurons : Int) {
    val weights = DoubleArray(numNeurons + 1)
    val neurons = arrayListOf(RadialNeuron(weights.size))

    fun connect(neuron : RadialNeuron) {
        if (neurons.size == weights.size) {
            throw IllegalStateException("add more neurons than it can operate on")
        }

        neurons.add(neuron)
    }

    fun output(x: Point) : Double {
        if (weights.size != neurons.size) {
            throw IllegalStateException("calculate without proper connected neuron number")
        }

        return neurons.mapIndexed { index, neuron ->  weights[index] * neuron.output(x) }.sum()
    }
}