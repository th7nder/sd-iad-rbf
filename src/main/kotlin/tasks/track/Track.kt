package tasks.track

import files.DataLoader
import math.Point
import math.input
import math.output
import math.project
import network.Network
import network.centers.CenterGenerator
import network.centers.KAverageGenerator
import network.sigmas.EqualSigmaGenerator
import network.sigmas.SigmaGenerator
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.lines.SeriesLines
import org.knowm.xchart.style.markers.SeriesMarkers
import utils.Charts

typealias DataSet = ArrayList<Pair<Point, Point>>

open class Track(val trainingData: DataSet, private val trainingIterations : Int, private val displayIterations: Int) {
    val alpha = 0.2
    val kAverageIterations = 50
    var derivatives = true

    fun createNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator, centerGenerator: CenterGenerator, dataSet: DataSet) = Network(numRadialNeurons, 2, dataSet, centerGenerator, sigmaGenerator, alpha, derivatives)
    fun createNetwork(numRadialNeurons: Int, trainingDataSet: DataSet) = createNetwork(numRadialNeurons, EqualSigmaGenerator(), KAverageGenerator(kAverageIterations), trainingDataSet)

    fun singleNetwork(numRadialNeurons: Int, trainingDataSet: DataSet = trainingData) : Network {
        val network = createNetwork(numRadialNeurons, trainingDataSet)
        network.train(trainingIterations) { i, error ->
            if (i % displayIterations == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error")
        }

        return network
    }
}


fun createChart(
    title: String,
    referenceData: List<Point>,
    inputData: List<Point>,
    outputData: List<Point>?
): XYChart {
    val chart =
        XYChartBuilder()
            .width(800)
            .height(800)
            .title(title).build()

    val test2 = chart.addSeries("Dane wzorcowe", referenceData.project(0), referenceData.project(1))
    test2.lineStyle = SeriesLines.NONE
    test2.marker = SeriesMarkers.CIRCLE

    val input = chart.addSeries("Przed nauką", inputData.project(0), inputData.project(1))
    input.lineStyle = SeriesLines.NONE
    input.marker = SeriesMarkers.CIRCLE

    if (outputData != null) {
        val test = chart.addSeries("Po nauce", outputData.project(0), outputData.project(1))
        test.lineStyle = SeriesLines.NONE
        test.marker = SeriesMarkers.CIRCLE
    }

    return chart
}


fun main() {
    val datas = arrayListOf(
        DataLoader.loadCSV("new/1.csv", 3, 2),
        DataLoader.loadCSV("new/2.csv", 3, 2),
        DataLoader.loadCSV("new/3.csv", 3, 2),
        DataLoader.loadCSV("new/4.csv", 3, 2),
        DataLoader.loadCSV("new/5.csv", 3, 2),
        DataLoader.loadCSV("new/6.csv", 3, 2),
        DataLoader.loadCSV("new/7.csv", 3, 2),
        DataLoader.loadCSV("new/8.csv", 3, 2),
        DataLoader.loadCSV("new/9.csv", 3, 2),
        DataLoader.loadCSV("new/10.csv", 3, 2),
        DataLoader.loadCSV("new/11.csv", 3, 2),
        DataLoader.loadCSV("new/12.csv", 3, 2)
    )

    val filtered = datas.map { data ->
        val average = data.sumByDouble {
            val first = Point(doubleArrayOf(it.first.coordinates[1], it.first.coordinates[2]))
           first.distance(it.second)
            } / data.size
        println(average)

        val filtered = data.filter {
            val first = Point(doubleArrayOf(it.first.coordinates[1], it.first.coordinates[2]))
            val second = Point(it.second)
            first.distance(second) < average / 1.5
        }
        filtered
    }

    val trainingData = ArrayList<Pair<Point, Point>>()
    for (data in filtered) {
        trainingData.addAll(data)
    }

    val chartTraining  = createChart("Dane treningowe", trainingData.output(), trainingData.input().map {
        Point(doubleArrayOf(it.coordinates[1], it.coordinates[2]))
    }, null)
    Charts.saveChart("training", chartTraining)

    val network = Track(trainingData, 3000, 100).singleNetwork(10)

    val testData = DataLoader.loadCSV("new/test.csv", 3, 2)
    val outputData = testData.input().map {
        Point(network.output(it))
    }

    val chart = createChart("Korekta toru robota - RBF", testData.output(), testData.input().map {
        Point(doubleArrayOf(it.coordinates[1], it.coordinates[2]))
    }, outputData)
    Charts.saveChart("test", chart)
}