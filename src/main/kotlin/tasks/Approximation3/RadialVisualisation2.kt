package tasks.Approximation3

import math.Point
import network.Network
import network.sigmas.EqualSigmaGenerator
import network.sigmas.TooBigSigmaGenerator
import network.sigmas.TooSmallSigmaGenerator
import org.knowm.xchart.XYChart
import org.knowm.xchart.style.markers.SeriesMarkers
import utils.Charts
import java.awt.Color

class RadialVisualisation2 : Approximation3() {
    val folder = "approximation3/2/"
    val radialNeurons = 11

    override fun plotNetwork(title: String, networks: List<Network>): XYChart {
        val chart = super.plotNetwork(title, networks)

        val values = ArrayList<ArrayList<Double>>()
        for (network in networks) {
            val outputNeuron = network.outputLayer.first()
            for (i in 1..network.numRadialNeurons) {
                values.add(ArrayList())
            }

            arguments.forEach {
                val outputs = outputNeuron.weigh(Point(it))
                for (index in 1..network.numRadialNeurons) {
                    values[index - 1].add(outputs[index])
                }
            }
        }

        for ( (index, function) in values.withIndex()) {
            val series = chart.addSeries("Radialny $index", arguments, function)
            series.marker = SeriesMarkers.NONE
            series.lineColor = Color.RED
            series.lineWidth = 0.5f
            series.isShowInLegend = false
        }

        return chart
    }


    fun small() {
        val chart = plotNetwork("Sieć RBF - za mała sigma", arrayListOf(singleNetwork(radialNeurons, TooSmallSigmaGenerator())))
        Charts.saveChart("$folder/small", chart)
    }

    fun big() {
        val chart = plotNetwork("Sieć RBF - za duża sigma", arrayListOf(singleNetwork(radialNeurons, TooBigSigmaGenerator())))
        Charts.saveChart("$folder/big", chart)
    }

    fun optimal() {
        val chart = plotNetwork("Sieć RBF - optymalna sigma", arrayListOf(singleNetwork(radialNeurons, EqualSigmaGenerator())))
        Charts.saveChart("$folder/optimal", chart)
    }

    override fun getDisplayIterations() = 1
}

fun main() {
    val radialVisualisation2 = RadialVisualisation2()
//    radialVisualisation2.optimal()
   // radialVisualisation2.small()
    radialVisualisation2.big()
}