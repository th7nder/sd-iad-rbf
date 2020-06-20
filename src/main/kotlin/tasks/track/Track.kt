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
import java.io.FileWriter
import kotlin.math.pow
import kotlin.math.sqrt

typealias DataSet = ArrayList<Pair<Point, Point>>

open class Track(val trainingData: DataSet, private val trainingIterations : Int, private val displayIterations: Int) {
    val alpha = 0.5
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
        DataLoader.loadCSV("new/1.csv", 2, 2),
        DataLoader.loadCSV("new/2.csv", 2, 2),
        DataLoader.loadCSV("new/3.csv", 2, 2),
        DataLoader.loadCSV("new/4.csv", 2, 2),
        DataLoader.loadCSV("new/5.csv", 2, 2),
        DataLoader.loadCSV("new/6.csv", 2, 2),
        DataLoader.loadCSV("new/7.csv", 2, 2),
        DataLoader.loadCSV("new/8.csv", 2, 2),
        DataLoader.loadCSV("new/9.csv", 2, 2),
        DataLoader.loadCSV("new/10.csv", 2, 2),
        DataLoader.loadCSV("new/11.csv", 2, 2),
        DataLoader.loadCSV("new/12.csv", 2, 2)
    )

    val sum = datas.sumByDouble { data -> data.sumByDouble { it.first.distance(it.second) }}
    val count = datas.sumBy { it.size }
    val average = sum / count


//    val filtered = datas.map { data ->
//        val filtered = data.filter {
//            val second = Point(it.second)
//            it.first.distance(second) < average
//        }
//        filtered
//    }

    val trainingData = ArrayList<Pair<Point, Point>>()
    for (data in datas) {
        trainingData.addAll(data)
    }

    val chartTraining  = createChart("Dane treningowe", trainingData.output(), trainingData.input(), null)
    Charts.saveChart("training", chartTraining)

    val network = Track(trainingData, 5000, 100).singleNetwork(15)

    val testData = DataLoader.loadCSV("new/test.csv", 2, 2)
    val outputData = testData.input().map {
        Point(network.output(it))
    }

    val chart = createChart("Korekta toru robota - RBF", testData.output(), testData.input(), outputData)
    Charts.saveChart("test", chart)

    val fw = FileWriter("data/results.csv")
    (outputData zip testData.output()).forEach {
        fw.append("${it.first.x()};${it.first.y()};${it.second.x()};${it.second.y()}\n")
    }
    fw.close()
}