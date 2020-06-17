package network

import math.Point

class NetworkV2 {
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


        var bs : List<Double> = ArrayList()
        for ((index, layer) in layers.withIndex().reversed()) {
            bs = if (index == layers.size - 1) {
                layer.backPropagate(outputs[index], outputs[index - 1], expected)
            } else {
                if (index == 0) {
                    layer.backPropagate(x, bs, layers[index + 1])
                } else {
                    layer.backPropagate(outputs[index - 1], bs, layers[index + 1])
                }
            }
        }

        return outputs.last()
    }
}