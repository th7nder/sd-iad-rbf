package network

import math.Point

class Network(dimension: Int, numRadialNeurons: Int, numLinearNeurons: Int) {
    val radialLayer = ArrayList<RadialNeuron>(numRadialNeurons)
    val outputLayer = ArrayList<LinearNeuron>(numLinearNeurons)

    init {
        for (i in 1..numRadialNeurons) {
            radialLayer.add(RadialNeuron(dimension))
        }

        for (i in 1..numLinearNeurons) {
            val linearNeuron = LinearNeuron(dimension, numRadialNeurons)
            linearNeuron.connect(radialLayer)
            outputLayer.add(linearNeuron)
        }
    }

    fun output(x: Point) : List<Double> = outputLayer.map { it.output(x) }
}