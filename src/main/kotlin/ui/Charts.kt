package utils


import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.markers.SeriesMarkers
import java.awt.Color


class Charts {
    companion object {
        fun createFirstWarmupChart(
            main : Pair<List<Double>, List<Double>>,
            partials: List<Pair<List<Double>, List<Double>>>,
            title: String
        ): XYChart {
            val chart =
                XYChartBuilder()
                    .width(800)
                    .height(800)
                    .title(title).build()
            //chart.styler.xAxisMin = 0.0
            //chart.styler.xAxisMax = 10.0

            //chart.styler.yAxisMin = -10.0
            //chart.styler.yAxisMax = 10.0



            for ( (idx, partial) in partials.withIndex()) {
                val partialSeries = chart.addSeries("Funkcje neuronów $idx", partial.first, partial.second)
                partialSeries.marker = SeriesMarkers.NONE
                partialSeries.lineColor = Color.RED
                partialSeries.lineWidth = 0.5f
            }

            val seriesData = chart.addSeries("Funkcja RBF", main.first, main.second)
            seriesData.lineColor = Color.BLACK
            seriesData.marker = SeriesMarkers.NONE

            return chart
        }

        fun saveChart(file: String, chart : XYChart) {
            BitmapEncoder.saveBitmap(
                chart,
                "data/$file",
                BitmapEncoder.BitmapFormat.PNG
            )
        }

    }
}