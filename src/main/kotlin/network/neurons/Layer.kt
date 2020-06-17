package network.neurons

import math.Point

open class Layer(val neurons: List<Neuron>) {

    fun getNeuron(index: Int) : Neuron {
        return neurons[index]
    }

    fun getNumberNeurons() : Int {
        return neurons.size
    }

    fun output(x: Point) : Point {
        return Point(neurons.map { it.output(x) })
    }

    fun backPropagate(output: Point, expected: Point, previousOutput: Point) : List<Double> {
        val b = (output.coordinates zip expected.coordinates)
            .withIndex()
            .map {(index, pair) ->
                val neuron = neurons[index]
                (pair.first - pair.second) * neuron.derivative(neuron.a(previousOutput))
            }
        return b
    }

    fun backPropagate(upperB : List<Double>, layer: Layer, previousOutput: Point) : List<Double> {
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
        return b
    }
}