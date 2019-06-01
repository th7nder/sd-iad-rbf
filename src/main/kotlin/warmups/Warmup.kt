package warmups

import math.Point
import network.Network
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.markers.SeriesMarkers
import java.awt.Color

abstract class Warmup {
    open fun plotNetwork(arguments: List<Double>, network: Network) : XYChart {

        var values = arguments.map { network.output(Point(it)).x() }.toList()

        val partials = ArrayList<Pair<List<Double>, List<Double>>>()
        val outputNeuron = network.outputLayer.first()
        for (idx in 1..network.numRadialNeurons) {
            partials.add(
                Pair(
                    arguments,
                    arguments.map { outputNeuron.weigh(Point(it))[idx] }.toList()
                )
            )
        }

        return createChart(
            Pair(arguments, values),
            partials,
            "Sieć RBF"
        )
    }

    fun createChart(
        main : Pair<List<Double>, List<Double>>,
        partials: List<Pair<List<Double>, List<Double>>>,
        title: String
    ): XYChart {
        val chart =
            XYChartBuilder()
                .width(800)
                .height(800)
                .title(title).build()

        for ( (idx, partial) in partials.withIndex()) {
            val partialSeries = chart.addSeries("Funkcje neuronów $idx", partial.first, partial.second)
            partialSeries.marker = SeriesMarkers.NONE
            partialSeries.lineColor = Color.RED
            partialSeries.lineWidth = 0.5f
            partialSeries.isShowInLegend = idx == 0
        }

        val seriesData = chart.addSeries("Funkcja RBF", main.first, main.second)
        seriesData.lineColor = Color.BLACK
        seriesData.marker = SeriesMarkers.NONE

        return chart
    }
}