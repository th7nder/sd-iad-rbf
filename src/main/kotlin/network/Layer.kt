package network

import math.Point
import network.neurons.Neuron

open class Layer(private val neurons: List<Neuron>) {
    // TODO: momentum
    // TODO: online/offline
    private val alpha = 0.2

    fun getNeuron(index: Int) : Neuron {
        return neurons[index]
    }

    fun getNumberNeurons() : Int {
        return neurons.size
    }

    fun output(x: Point) : Point {
        return Point(neurons.map { it.output(x) })
    }

    fun backPropagate(output: Point, input: Point, expected: Point) : List<Double> {
        val b = (output.coordinates zip expected.coordinates)
            .withIndex()
            .map {(index, pair) ->
                val neuron = neurons[index]
                (pair.first - pair.second) * neuron.derivative(neuron.a(input))
            }
        applyChanges(b, input)
        return b
    }

    fun applyChanges(b: List<Double>, input: Point) {
        for (m in neurons.indices) {
            val neuron = neurons[m]
            val weights = neuron.weights
            for (n in weights.indices) {
                weights[n] = weights[n] - alpha * (b[m] * input.coordinates[n])
            }
        }
    }

    fun backPropagate(previousOutput: Point, upperB : List<Double>, layer: Layer) : List<Double> {
        val b = ArrayList<Double>()
        for (m in neurons.indices) {
            var sum = 0.0
            for (j in 0 until layer.getNumberNeurons()) {
                sum += upperB[j] * layer.getNeuron(j).weights[m]
            }
            b.add(
                sum * neurons[m].derivative(neurons[m].a(previousOutput))
            )
        }

        applyChanges(b, previousOutput)
        return b
    }
}