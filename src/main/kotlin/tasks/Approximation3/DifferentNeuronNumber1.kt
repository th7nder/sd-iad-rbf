package tasks.Approximation3

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import network.Network
import network.centers.FromDataGenerator
import network.sigmas.EqualSigmaGenerator
import network.sigmas.SigmaGenerator
import network.sigmas.TooBigSigmaGenerator
import network.sigmas.TooSmallSigmaGenerator
import utils.Charts

class DifferentNeuronNumber1 : Approximation3() {
    val numIterations = 6000
    val alpha = 0.5
    val folder = "approximation3/1/"

    fun singleNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator) : Network {
        val network = Network(numRadialNeurons, 1, trainingData, FromDataGenerator(), sigmaGenerator, alpha)
        network.train(numIterations) { i, error -> if (i % 500 == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error")}

        return network
    }

    fun singleSigma(sigmaGenerator: SigmaGenerator) : List<Network> {
        val routines = (1..41 step 10).map { GlobalScope.async {singleNetwork(it, sigmaGenerator)} }

        return runBlocking {
            routines.map { it.await() }
        }
    }

    fun chartOptimalSigma() {
        val chart = plotNetwork("Sieć RBF - optymalna sigma", singleSigma(EqualSigmaGenerator()))
        Charts.saveChart("$folder/optimal", chart)
    }

    fun chartSmallSigma() {
        val chart = plotNetwork("Sieć RBF - za mała sigma", singleSigma(TooSmallSigmaGenerator()))
        Charts.saveChart("$folder/small", chart)
    }

    fun chartBigSigma() {
        val chart = plotNetwork("Sieć RBF - za duża sigma", singleSigma(TooBigSigmaGenerator()))
        Charts.saveChart("$folder/big", chart)
    }
}

fun main() {
    val dnn = DifferentNeuronNumber1()
    //dnn.chartOptimalSigma()

    dnn.singleNetwork(11, TooBigSigmaGenerator())
    /*val second = GlobalScope.async { dnn.chartSmallSigma() }
    val third = GlobalScope.async { dnn.chartBigSigma() }

    runBlocking {
        second.await()
        third.await()
    }*/
}