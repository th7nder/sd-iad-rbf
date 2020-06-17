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
        var output : Point = Point(x.dimension())
        for (i in 0..layer) {
            input = layers[i].output(input)
            output = input
        }

        return output
    }

    fun train(trainingData: List<Pair<Point, Point>>) {
        val lastLayerNeuronsWeightDelta = ArrayList<DoubleArray>()
        val lastLayerBiases = DoubleArray(layers[1].getNumberNeurons())
        for (i in 0 until layers[1].getNumberNeurons()) {
            lastLayerNeuronsWeightDelta.add(DoubleArray(layers[1].neurons[0].weights.size))
        }
        val firstLayerNeuronsWeightDelta = ArrayList<DoubleArray>()
        val firstLayerBiases = DoubleArray(layers[0].getNumberNeurons())
        for (i in 0 until layers[0].getNumberNeurons()) {
            firstLayerNeuronsWeightDelta.add(DoubleArray(layers[0].neurons[0].weights.size))
        }

        for (data in trainingData) {
            val (input, expected) = data
            val b = layers[1].backPropagate(output(input), expected, output(0, input))
            for (m in lastLayerNeuronsWeightDelta.indices) {
                lastLayerBiases[m] = lastLayerBiases[m] + b[m]
                delta(b, m, output(0, input), lastLayerNeuronsWeightDelta[m])
            }

            val bFirst = layers[0].backPropagate(b, layers[1], input)
            for (m in firstLayerNeuronsWeightDelta.indices) {
                firstLayerBiases[m] = firstLayerBiases[m] + bFirst[m]
                delta(bFirst, m, input, firstLayerNeuronsWeightDelta[m])
            }
        }

        for (m in lastLayerNeuronsWeightDelta.indices) {
            val changes = lastLayerNeuronsWeightDelta[m]
            lastLayerBiases[m] = lastLayerBiases[m] / trainingData.size
            layers[1].neurons[m].bias -= alpha * lastLayerBiases[m]
            for (i in changes.indices) {
                changes[i] = changes[i] / trainingData.size
                val weights = layers[1].neurons[m].weights
                weights[i] = weights[i] - alpha * changes[i]
            }
        }

        for (m in firstLayerNeuronsWeightDelta.indices) {
            val changes = firstLayerNeuronsWeightDelta[m]
            firstLayerBiases[m] = firstLayerBiases[m] / trainingData.size
            layers[0].neurons[m].bias -= alpha * firstLayerBiases[m]

            for (i in changes.indices) {
                changes[i] = changes[i] / trainingData.size
                val weights = layers[0].neurons[m].weights
                weights[i] = weights[i] - alpha * changes[i]
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