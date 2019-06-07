package network

import math.Point
import math.input
import math.output
import math.project
import network.centers.CenterGenerator
import network.sigmas.SigmaGenerator

class Network(val numRadialNeurons : Int, var alpha : Double) {
    private val radialLayer = ArrayList<RadialNeuron>()
    val outputLayer = ArrayList<LinearNeuron>()
    lateinit var trainingData : List<Pair<Point, Point>>
    private var derivatives = false


    constructor(dimension: Int, sigmas : List<Double>, centers: List<Point>, numLinearNeurons: Int) : this(sigmas.size, 0.0) {
        createRadialLayer(sigmas, centers)
        createLinearLayer(dimension, numLinearNeurons)
    }

    constructor(
        numRadialNeurons: Int,
        numLinearNeurons: Int,
        trainingData: List<Pair<Point, Point>>,
        centerGenerator: CenterGenerator,
        sigmaGenerator: SigmaGenerator,
        alpha: Double,
        derivatives: Boolean = false
    ) : this(numRadialNeurons, alpha) {
        this.trainingData = trainingData
        this.derivatives = derivatives
        val centers = centerGenerator.generate(numRadialNeurons, trainingData.input())
        val sigmas = sigmaGenerator.generate(centers)

        createRadialLayer(sigmas, centers)
        createLinearLayer(trainingData.input().first().dimension(), numLinearNeurons)
    }

    private fun createRadialLayer(sigmas: List<Double>, centers: List<Point>) {
        for (i in 0 until numRadialNeurons) {
            radialLayer.add(RadialNeuron(centers[i], sigmas[i]))
        }
    }

    private fun createLinearLayer(dimension: Int, numLinearNeurons: Int) {
        for (i in 1..numLinearNeurons) {
            val linearNeuron = LinearNeuron(dimension, numRadialNeurons, derivatives)
            linearNeuron.connect(radialLayer)
            outputLayer.add(linearNeuron)
        }
    }

    fun output(x: Point) : Point = Point(outputLayer.map { it.output(x) })

    fun step() {
        for ((index, linearNeuron) in outputLayer.withIndex()) {
            linearNeuron.step(trainingData.input(), trainingData.output().project(index), alpha)
        }
    }

    fun error(data: List<Pair<Point, Point>>) = data.sumByDouble { (input, expected) -> (output(input) - expected).pow(2.0).sum() } / (2.0 * data.size)
    fun error() = error(trainingData)

    fun train(error: Double, onIteration : ((i : Int, error: Double) -> Unit)? = null) {
        var i = 0
        do {
            step()
            i += 1
            val currentError = error()
            onIteration?.invoke(i, currentError)
        } while (currentError > error)
    }

    fun train(iters: Int,  onIteration : ((i : Int, error: Double) -> Unit)? = null) {
        for (i in 1..iters) {
            step()

            onIteration?.invoke(i, error())
        }
    }

}