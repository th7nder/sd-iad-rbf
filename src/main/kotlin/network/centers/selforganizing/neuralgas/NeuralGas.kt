package network.centers.selforganizing.neuralgas

import math.Point
import network.centers.selforganizing.Algorithm
import network.centers.selforganizing.common.Centroid
import network.centers.selforganizing.common.Influencer

class NeuralGas() : Algorithm("Gaz neuronowy") {
    lateinit var influencer : Influencer
    lateinit var alpha : (it: Int) -> Double

    constructor(data: List<Point>, centroids: List<Centroid>, influencer: Influencer, alpha: (it: Int) -> Double) : this() {
        this.influencer = influencer
        this.alpha = alpha
        setTrainingData(data, centroids)
    }

    private fun step(point: Point, iteration : Int) {
        val order = centroids.sortedBy { it.distance(point) }

        for ((index, neuron) in order.withIndex()) {
            neuron.update(
                neuron +
                        (point - neuron) * alpha(iteration) * influencer.influence(index, 0, iteration)
            )
        }
    }

    override fun step(iteration: Int) {
        data.shuffled().forEach {
            step(it, iteration)
        }
        assign()
    }
}