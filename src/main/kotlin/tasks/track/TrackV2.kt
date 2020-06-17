package tasks.track

import files.DataLoader
import math.Point
import network.neurons.Layer
import network.neurons.NetworkV2
import network.neurons.IdentityNeuron
import network.neurons.SigmoidNeuron
import java.util.*

fun main() {
    val datas = listOf(
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

    val filtered = datas.map { data ->
        val average = data.sumByDouble {
//            val first = Point(doubleArrayOf(it.first.coordinates[1], it.first.coordinates[2]))
            it.first.distance(it.second)
        } / data.size

        val filtered = data.filter {
//            val first = Point(doubleArrayOf(it.first.coordinates[1], it.first.coordinates[2]))
            val second = Point(it.second)
            it.first.distance(second) < average
        }
        filtered
    }
    val trainingData = ArrayList<Pair<Point, Point>>()
    for (data in filtered) {
        trainingData.addAll(data)
    }

    val network = NetworkV2()
    val inputLayer = Layer(
        (1..5).map { SigmoidNeuron(2) }
    )
    val outputLayer = Layer(
        listOf(
            IdentityNeuron(5),
            IdentityNeuron(5)
        )
    )
    network.layers.add(inputLayer)
    network.layers.add(outputLayer)
    
    for (iteration in 1..30000) {
        if (iteration % 100 == 0) {
            println("$iteration ${network.error(trainingData)}")
        }
        network.train(trainingData)
    }
}