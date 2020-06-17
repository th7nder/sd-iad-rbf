package network.neurons

import kotlin.math.exp

class TanhNeuron(
    numInputs: Int
) : Neuron(numInputs) {

    override fun f(x : Double) : Double {
        return (exp(x) - exp(-x)) / (exp(x) + exp(-x))
    }

    override fun derivative(x: Double) : Double {
        return 1 - (f(x) * f(x))
    }
}