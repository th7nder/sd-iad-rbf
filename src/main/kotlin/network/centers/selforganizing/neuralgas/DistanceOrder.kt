package network.centers.selforganizing.neuralgas

import network.centers.selforganizing.common.Influencer
import kotlin.math.exp

class DistanceOrder(private val lambda: (iteration: Int) -> Double) : Influencer {

    override fun influence(neuronIndex: Int, winningNeuronIndex: Int, iteration: Int): Double {
        return exp(-neuronIndex / lambda(iteration))
    }

    override fun toString(): String {
        return "DistanceOrder"
    }

}