package network

import math.Point

class Network(
    dimension: Int,
    sigmas : List<Double>,
    centers: List<Point>,
    numLinearNeurons: Int
) {
    val numRadialNeurons = sigmas.size

    private val radialLayer = ArrayList<RadialNeuron>(numRadialNeurons)
    val outputLayer = ArrayList<LinearNeuron>(numLinearNeurons)

    init {
        for (i in 0 until numRadialNeurons) {
            radialLayer.add(RadialNeuron(centers[i], sigmas[i]))
        }

        for (i in 1..numLinearNeurons) {
            val linearNeuron = LinearNeuron(dimension, numRadialNeurons)
            linearNeuron.connect(radialLayer)
            outputLayer.add(linearNeuron)
        }
    }

    fun output(x: Point) : List<Double> = outputLayer.map { it.output(x) }
}