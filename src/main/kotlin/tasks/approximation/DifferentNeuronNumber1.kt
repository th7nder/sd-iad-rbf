package tasks.approximation

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import network.Network
import network.sigmas.EqualSigmaGenerator
import network.sigmas.SigmaGenerator
import network.sigmas.TooBigSigmaGenerator
import network.sigmas.TooSmallSigmaGenerator
import utils.Charts

class DifferentNeuronNumber1 : Approximation3() {
    fun getFolder() : String {
        val folder = "approximation3/1/"
        if (derivatives) {
            return "$folder/derivatives"
        }

        return folder
    }

    fun singleSigma(sigmaGenerator: SigmaGenerator) : List<Network> {
        val routines = (1..41 step 10).map { GlobalScope.async {singleNetwork(it, sigmaGenerator)} }

        return runBlocking {
            routines.map { it.await() }
        }
    }

    fun chartOptimalSigma() {
        val chart = plotNetwork("Sieć RBF - optymalna sigma", singleSigma(EqualSigmaGenerator()))
        Charts.saveChart("${getFolder()}/optimal", chart)
    }

    fun chartSmallSigma() {
        val chart = plotNetwork("Sieć RBF - za mała sigma", singleSigma(TooSmallSigmaGenerator()))
        Charts.saveChart("${getFolder()}/small", chart)
    }

    fun chartBigSigma() {
        val chart = plotNetwork("Sieć RBF - za duża sigma", singleSigma(TooBigSigmaGenerator()))
        Charts.saveChart("${getFolder()}/big", chart)
    }
}

fun main() {
    val dnn = DifferentNeuronNumber1()
    dnn.derivatives = true

    val first = GlobalScope.async { dnn.chartOptimalSigma() }
    val second = GlobalScope.async { dnn.chartSmallSigma() }
    val third = GlobalScope.async { dnn.chartBigSigma() }

    runBlocking {
        first.await()
        second.await()
        third.await()
    }
}