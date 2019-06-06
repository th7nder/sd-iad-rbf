package tasks.classification

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import math.*
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.lines.SeriesLines
import org.knowm.xchart.style.markers.SeriesMarkers
import utils.Charts
import java.awt.Color

class DecisionAreas : Classification() {
    val folder = "classification4/3"
    val selectedCombinations = combinations.filter { it.first == 2 }

    fun singleChart(numRadialNeurons: Int, combination: Combination) {
        val trainingData = projectData(trainingData, combination)
        val testData = projectData(testData, combination)

        val network = createNetwork(numRadialNeurons, trainingData)
        network.train(getTrainingIterations()) { i, error ->
            if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error")
        }

        val trainingX1 = trainingData.input().project(0)
        val trainingX2 = trainingData.input().project(1)

        val testX1 = testData.input().project(0)
        val testX2 = testData.input().project(1)

        val minX1 = Math.min(trainingX1.min()!!, testX1.min()!!)
        val maxX1 = Math.max(trainingX1.max()!!, testX1.max()!!)

        val minX2 = Math.min(trainingX2.min()!!, testX2.min()!!)
        val maxX2 = Math.max(trainingX2.max()!!, testX2.max()!!)


        val X1 = Utils.arange(minX1, maxX1, 0.01)
        val X2 = Utils.arange(minX2, maxX2, 0.01)

        val map = listOf(ArrayList<Point>(), ArrayList(), ArrayList())
        for (x1 in X1) {
            for (x2 in X2) {
                val point = Point(listOf(x1, x2))
                val output = network.output(point)
                val group = Utils.argmax(output.coordinates)
                map[group].add(point)
            }
        }

        val assignedPoints = listOf(ArrayList<Point>(), ArrayList(), ArrayList())
        for (row in trainingData + testData) {
            val group = Utils.argmax(row.second.coordinates)
            assignedPoints[group] += row.first
        }

        val selectedInputs = combination.second.joinToString(",")
        Charts.saveChart(
            "$folder/${combination.first}_$selectedInputs",
            createChart("Rozkład punktów z podziałem na klasy", combination, map, assignedPoints)
        )
    }

    private fun createChart(
        title: String,
        combination: Combination,
        map: List<ArrayList<Point>>,
        assignedPoints: List<ArrayList<Point>>
    ) : XYChart {
        val chart =
            XYChartBuilder()
                .width(800)
                .height(800)
                .xAxisTitle("Wejście nr ${combination.second[0]}")
                .yAxisTitle("Wejście nr ${combination.second[1]}")
                .title(title).build()

        val colors = listOf(
            Color(77, 202, 77),
            Color(70, 141, 247),
            Color(239, 146, 53)
        )

        val backgroundColors = listOf(
            Color(192, 253, 91),
            Color(88, 188, 249),
            Color(249, 226, 187)
        )

        map.filter { it.size > 0 }.forEachIndexed { index, data ->
            val series = chart.addSeries("Klasa ${index + 1}", data.project(0), data.project(1))
            series.lineStyle = SeriesLines.NONE
            series.marker = SeriesMarkers.CIRCLE
            series.markerColor = backgroundColors[index]
        }


        assignedPoints.forEachIndexed { index, data ->
            val series = chart.addSeries("Klasa ${index + 1} v2", data.project(0), data.project(1))
            series.lineStyle = SeriesLines.NONE
            series.markerColor = colors[index]
        }


        return chart
    }


    val numRadialNeurons = 11
    fun generateAllCombinations() {
        val asyncs = selectedCombinations.map { GlobalScope.async { singleChart(numRadialNeurons, it) }}
        runBlocking {
            asyncs.map { it.await() }
        }

    }

}

fun main() {
    val decisionAreas = DecisionAreas()
    decisionAreas.generateAllCombinations()
}