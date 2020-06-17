package network.neurons

import kotlin.math.exp

class SigmoidNeuron(
    numInputs: Int
) : Neuron(numInputs) {

    override fun f(x : Double) : Double {
        return 1.0 / (1 + exp(-x))
    }

    override fun derivative(x: Double) : Double {
        return f(x) * (1 - f(x))
    }
}