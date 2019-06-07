package network

import math.Point
import kotlin.random.Random

class LinearNeuron(dimension: Int, numPreviousNeurons: Int, private val derivatives: Boolean = false) {
    private val weights = DoubleArray(numPreviousNeurons + 1)
    private val neurons = arrayListOf(RadialNeuron(Point(dimension),0.0))

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

    private fun outputNeurons(x: Point) = neurons.mapIndexed { index, neuron ->
        if (index == 0) {
            1.0
        } else {
            neuron.z(x)
        }
    }

    fun weigh(x: Point) : List<Double> {
        return outputNeurons(x).mapIndexed { index, output -> output * weights[index]}
    }

    fun output(x: Point) : Double {
        if (weights.size != neurons.size) {
            throw IllegalStateException("calculate without proper connected neuron number")
        }

        return weigh(x).sum()
    }


    fun step(inputs : List<Point>, expected : List<Double>, alpha: Double) {
        val adjustments = DoubleArray(weights.size)

        val differences = (inputs zip expected).map {(input, expected) -> Pair(input, output(input) - expected) }

        differences.forEach { (input, difference) ->  outputNeurons(input).forEachIndexed { index, output -> adjustments[index] += output * difference} }

        for (index in adjustments.indices) {
            adjustments[index] = adjustments[index] / inputs.size
        }

        if (derivatives) {
            // skip first neuron which works as bias
            for (index in 1 until neurons.size) {
                neurons[index].step(differences, alpha, weights[index])
            }
        }


        for (index in weights.indices) {
            weights[index] = weights[index] - (alpha * adjustments[index])
        }
    }
}