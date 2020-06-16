package tasks.track

import files.DataLoader
import math.Point
import network.Network
import network.centers.CenterGenerator
import network.centers.KAverageGenerator
import network.sigmas.EqualSigmaGenerator
import network.sigmas.SigmaGenerator
import java.util.*
import kotlin.collections.ArrayList

typealias DataSet = ArrayList<Pair<Point, Point>>

open class Track(val trainingData: DataSet) {
    val alpha = 0.2
    val kAverageIterations = 100
    var derivatives = true


    fun createNetwork(numRadialNeurons: Int, sigmaGenerator: SigmaGenerator, centerGenerator: CenterGenerator, dataSet: DataSet) = Network(numRadialNeurons, 2, dataSet, centerGenerator, sigmaGenerator, alpha, derivatives)
    fun createNetwork(numRadialNeurons: Int, trainingDataSet: DataSet) = createNetwork(numRadialNeurons, EqualSigmaGenerator(), KAverageGenerator(kAverageIterations), trainingDataSet)

    fun singleNetwork(numRadialNeurons: Int, trainingDataSet: DataSet = trainingData) : Network {
        val network = createNetwork(numRadialNeurons, trainingDataSet)
        network.train(getTrainingIterations()) { i, error ->
            if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error")
        }

        return network
    }

    open fun getTrainingIterations(): Int = 20000
    open fun getDisplayIterations(): Int = 100
}

fun main() {
    val datas = arrayListOf(
        DataLoader.loadCSV("car_1.csv", 2, 2),
        DataLoader.loadCSV("car_2.csv", 2, 2),
        DataLoader.loadCSV("car_3.csv", 2, 2),
        DataLoader.loadCSV("car_4.csv", 2, 2)
//        DataLoader.loadCSV("car_5.csv", 2, 2),
//        DataLoader.loadCSV("car_6.csv", 2, 2),
//        DataLoader.loadCSV("car_7.csv", 2, 2),
//        DataLoader.loadCSV("car_8.csv", 2, 2),
//        DataLoader.loadCSV("car_9.csv", 2, 2),
//        DataLoader.loadCSV("car_10.csv", 2, 2),
//        DataLoader.loadCSV("car_11.csv", 2, 2),
//        DataLoader.loadCSV("car_12.csv", 2, 2)
    )

    val filtered = datas.map { data ->
        println("XDDDDD")
        val min = data.minBy { it.first.distance(it.second) }
        println(min!!.first.distance(min.second))
        val max = data.maxBy { it.first.distance(it.second) }
        println(max!!.first.distance(max.second))
        val average = data.sumByDouble { it.first.distance(it.second) } / data.size
        println(average)

        val filtered = data.filter {
            it.first.distance(it.second) > average
        }
        filtered
    }

    val trainingData = ArrayList<Pair<Point, Point>>()
    for (data in filtered) {
        trainingData.addAll(data)
    }

    Track(trainingData).singleNetwork(40)
}