package network

import math.Point

class Network(val numRadialNeurons : Int) {
    private val radialLayer = ArrayList<RadialNeuron>()
    val outputLayer = ArrayList<LinearNeuron>()

    constructor(dimension: Int, sigmas : List<Double>, centers: List<Point>, numLinearNeurons: Int) : this(sigmas.size) {
        createRadialLayer(sigmas, centers)
        createLinearLayer(dimension, numLinearNeurons)
    }

    private fun createRadialLayer(sigmas: List<Double>, centers: List<Point>) {
        for (i in 0 until numRadialNeurons) {
            radialLayer.add(RadialNeuron(centers[i], sigmas[i]))
        }
    }

    private fun createLinearLayer(dimension: Int, numLinearNeurons: Int) {
        for (i in 1..numLinearNeurons) {
            val linearNeuron = LinearNeuron(dimension, numRadialNeurons)
            linearNeuron.connect(radialLayer)
            outputLayer.add(linearNeuron)
        }
    }



    fun output(x: Point) : List<Double> = outputLayer.map { it.output(x) }
}