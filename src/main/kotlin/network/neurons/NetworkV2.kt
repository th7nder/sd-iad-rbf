package network.neurons

import math.Point

class NetworkV2 {
    // TODO: online/offline
    private val alpha = 0.1
    private val momentum = 0.05
    val layers = ArrayList<Layer>()
    private var previousDeltas : List<ArrayList<DoubleArray>>? = null
    private var previousBiases : List<DoubleArray>? = null

    fun output(x : Point) : Point {
        return output(layers.size - 1, x)
    }

    fun output(layer: Int, x: Point) : Point {
        var input = x
        var output = Point(x.dimension())
        for (i in 0..layer) {
            input = layers[i].output(input)
            output = input
        }

        return output
    }

    fun train(trainingData: List<Pair<Point, Point>>) {
        val deltas = ArrayList<ArrayList<DoubleArray>>()
        val biases = ArrayList<DoubleArray>()
        for (layer in layers) {
            val neuronsWeights = ArrayList<DoubleArray>()
            for (i in 0 until layer.getNumberNeurons()) {
                neuronsWeights.add(DoubleArray(layer.neurons[0].weights.size))
            }
            biases.add(DoubleArray(layer.getNumberNeurons()))
            deltas.add(neuronsWeights)
        }

        // TODO: optimize outputs
        for (data in trainingData.shuffled()) {
            val (input, expected) = data

            val outputs = ArrayList<Point>()
            for ((index) in layers.withIndex()) {
                outputs.add(output(index, input))
            }

            var previousB : List<Double> = ArrayList()
            for ((l, layer) in layers.withIndex().reversed()) {
                var b: List<Double>
                val previousOutput = if (l != 0) {
                    outputs[l - 1]
//                    output(l - 1, input)
                } else {
                    input
                }

                if (l == layers.size - 1) {
                    b = layer.backPropagate(output(input), expected, previousOutput)
                } else {
                    b = layer.backPropagate(previousB, layers[l + 1], previousOutput)
                }
                for (m in layer.neurons.indices) {
                    biases[l][m] = biases[l][m] + b[m]
                    delta(b, m, previousOutput, deltas[l][m])
                }
                previousB = b
            }
        }

        for ((l, layer) in layers.withIndex()) {
            for ((m, neuron) in layer.neurons.withIndex()) {
                biases[l][m] = biases[l][m] / trainingData.size
                if (previousBiases != null) {
                    neuron.bias = neuron.bias - alpha * biases[l][m] + momentum * previousBiases!![l][m]
                } else {
                    neuron.bias = neuron.bias - alpha * biases[l][m]

                }
                for ((n, weight) in neuron.weights.withIndex()) {
                    deltas[l][m][n] = deltas[l][m][n] / trainingData.size
                    if (previousDeltas != null) {
                        neuron.weights[n] = weight - alpha * deltas[l][m][n] + momentum * previousDeltas!![l][m][n]
                    } else {
                        neuron.weights[n] = weight - alpha * deltas[l][m][n]
                    }
                }
            }
        }

        previousBiases = biases
        previousDeltas = deltas
    }

    fun delta(b: List<Double>, m: Int, input: Point, weights: DoubleArray) {
        for (n in weights.indices) {
            weights[n] += (b[m] * input.coordinates[n])
        }
    }

    fun error(data: List<Pair<Point, Point>>) = data.sumByDouble { (input, expected) ->
        (output(input) - expected).pow(2.0).sum()
    } / (2.0 * data.size)
}