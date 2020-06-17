package network

import math.Point

class NetworkV2 {
    // TODO: momentum
    // TODO: online/offline
    // TODO: bias
    private val alpha = 0.2
    val layers = ArrayList<Layer>()

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
        for (layer in layers) {
            val neuronsWeights = ArrayList<DoubleArray>()
            for (i in 0 until layer.getNumberNeurons()) {
                neuronsWeights.add(DoubleArray(layer.neurons[0].weights.size))
            }
            deltas.add(neuronsWeights)
        }

//        val lastLayerNeuronsWeightDelta = ArrayList<DoubleArray>()
//        val lastLayerBiases = DoubleArray(layers[1].getNumberNeurons())
//        for (i in 0 until layers[1].getNumberNeurons()) {
//            lastLayerNeuronsWeightDelta.add(DoubleArray(layers[1].neurons[0].weights.size))
//        }
//        val firstLayerNeuronsWeightDelta = ArrayList<DoubleArray>()
//        val firstLayerBiases = DoubleArray(layers[0].getNumberNeurons())
//        for (i in 0 until layers[0].getNumberNeurons()) {
//            firstLayerNeuronsWeightDelta.add(DoubleArray(layers[0].neurons[0].weights.size))
//        }

        // TODO: optimize outputs
        for (data in trainingData) {
            val (input, expected) = data

            var previousB : List<Double> = ArrayList()
            for ((l, layer) in layers.withIndex().reversed()) {
                var b: List<Double>
                val previousOutput = if (l != 0) {
                    output(l - 1, input)
                } else {
                    input
                }

                if (l == layers.size - 1) {
                    b = layer.backPropagate(output(input), expected, previousOutput)
                } else {
                    b = layer.backPropagate(previousB, layers[l + 1], previousOutput)
                }
                for (m in layer.neurons.indices) {
                    // TODO: bias
                    delta(b, m, previousOutput, deltas[l][m])
                }
                previousB = b
            }
//            val b = layers[1].backPropagate(output(input), expected, output(0, input))
//            for (m in lastLayerNeuronsWeightDelta.indices) {
//                lastLayerBiases[m] = lastLayerBiases[m] + b[m]
//                delta(b, m, output(0, input), lastLayerNeuronsWeightDelta[m])
//            }
//
//            val bFirst = layers[0].backPropagate(b, layers[1], input)
//            for (m in firstLayerNeuronsWeightDelta.indices) {
//                firstLayerBiases[m] = firstLayerBiases[m] + bFirst[m]
//                delta(bFirst, m, input, firstLayerNeuronsWeightDelta[m])
//            }
        }

        for ((l, layer) in layers.withIndex()) {
            for ((m, neuron) in layer.neurons.withIndex()) {
                // TODO: bias
                for ((n, weight) in neuron.weights.withIndex()) {
                    deltas[l][m][n] = deltas[l][m][n] / trainingData.size
                    neuron.weights[n] = weight - alpha * deltas[l][m][n]
                }
            }
        }
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