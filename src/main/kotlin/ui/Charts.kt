package utils


import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.markers.SeriesMarkers
import java.awt.Color


class Charts {
    companion object {
        fun createChart(arguments: List<Double>, values: List<Double>, title: String): XYChart {
            val chart =
                XYChartBuilder()
                    .width(800)
                    .height(800)
                    .title(title).build()
            chart.styler.xAxisMin = 0.0
            chart.styler.xAxisMax = 10.0

            chart.styler.yAxisMin = -10.0
            chart.styler.yAxisMax = 10.0

            val seriesData = chart.addSeries("Funkcja RBF", arguments, values)

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