package network.neurons

class IdentityNeuron(
    numInputs: Int
) : Neuron(numInputs) {

    override fun f(x : Double) : Double {
       return x
    }

    override fun derivative(x: Double) : Double {
        return 1.0
    }
}