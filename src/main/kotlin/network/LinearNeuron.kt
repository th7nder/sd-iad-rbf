package network

import math.Point
import kotlin.random.Random

class LinearNeuron(dimension: Int, numPreviousNeurons: Int) {
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

    fun weigh(x: Point) : List<Double> {
        return neurons.mapIndexed { index, neuron ->
            if (index == 0) {
                weights[index] * 1
            } else {
                weights[index] * neuron.z(x)
            }
        }
    }

    fun output(x: Point) : Double {
        if (weights.size != neurons.size) {
            throw IllegalStateException("calculate without proper connected neuron number")
        }

        return weigh(x).sum()
    }


    fun step(inputs : List<Point>, expected : List<Double>, alpha: Double) {
        for (weightIndex in weights.indices) {
            var sum = 0.0
            for ((input, expected) in inputs zip expected) {
                val difference = output(input) - expected
                val weight = neurons[weightIndex].z(input)
                val multiplied = difference * weight
                sum += multiplied
            }
            sum /= inputs.size
            weights[weightIndex] -= alpha * sum
        }
    }
}