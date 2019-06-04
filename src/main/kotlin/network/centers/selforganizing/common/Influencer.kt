package network.centers.selforganizing.common

interface Influencer {
    fun influence(neuronIndex : Int, winningNeuronIndex: Int, iteration: Int = 0) : Double
}