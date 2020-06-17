package network

import math.Point
import network.neurons.Neuron

class NetworkV2 {
    // TODO: momentum
    // TODO: online/offline
    private val alpha = 0.01
    val layers = ArrayList<Layer>()

    fun output(x : Point, expected: Point? = null) : Point {
        var input = x
        val outputs = ArrayList<Point>()
        for (layer in layers) {
            input = layer.output(input)
            outputs.add(input)
        }
        if (expected == null) {
            return outputs.last()
        }


        var bs : ArrayList<List<Double>> = ArrayList()
        for ((index, layer) in layers.withIndex().reversed()) {
            bs.add(if (index == layers.size - 1) {
                layer.backPropagate(outputs[index], outputs[index - 1], expected)
            } else {
                if (index == 0) {
                    layer.backPropagate(x, bs.last(), layers[index + 1])
                } else {
                    layer.backPropagate(outputs[index - 1], bs.last(), layers[index + 1])
                }
            })
        }

        applyChanges(bs[0], outputs[0], layers[1].neurons)
        applyChanges(bs[1], x, layers[0].neurons)

        return outputs.last()
    }

    fun applyChanges(b: List<Double>, input: Point, neurons: List<Neuron>) {
        for (m in neurons.indices) {
            val neuron = neurons[m]
            for (n in neuron.updates.indices) {
                neuron.updates[n] -= alpha * (b[m] * input.coordinates[n])
            }
        }
    }

    fun error(data: List<Pair<Point, Point>>) = data.sumByDouble { (input, expected) -> (output(input) - expected).pow(2.0).sum() } / (2.0 * data.size)
}