package tasks.classification

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import math.Utils
import tasks.approximation.Table3

class Table : Classification() {
    val folder = "data/classification4/2"
    private val tableIters = 100
    private val combination = Combination(1, listOf(3))
    private val selectedTrainingData = projectData(trainingData, combination)
    private val selectedTestData = projectData(testData, combination)


    fun singleRow(numRadialNeurons: Int) : Statistics {
        val asyncs = (1..tableIters).map { GlobalScope.async { singleNetwork(numRadialNeurons, selectedTrainingData)} }

        val networks = runBlocking {
            asyncs.map { it.await() }
        }

        val trainingPercentages = networks.map { percentage(selectedTrainingData, it) }
        val testPercentages =  networks.map { percentage(selectedTestData, it) }

        return Statistics(
            numRadialNeurons,
            trainingPercentages.average(),
            Utils.standardDeviation(trainingPercentages),
            testPercentages.average(),
            Utils.standardDeviation(testPercentages)
        )
    }

    fun buildTable() : Table {
        val statistics = (1..41 step 5).map {
            val statistics = singleRow(it)
            saveStatistics(statistics)
            statistics
        }

        return Table(statistics)
    }


    data class Statistics(
        val numRadialNeurons: Int,
        val trainingAverageClassification : Double,
        val trainingStandardDeviation: Double,
        val testAverageClassification : Double,
        val testStandardDeviation: Double
    ) {
        override fun toString(): String {
            return "$numRadialNeurons & $trainingAverageClassification & $trainingStandardDeviation & $testAverageClassification & $testStandardDeviation \\\\"
        }
    }

    class Table(val list: List<Statistics>) {
        override fun toString(): String {
            val builder = StringBuilder()
            builder.append("\\textbf{K} & \\textbf{\$avg(\\p_a)\$} & \\textbf{\$\\sigma(\\p_a)\$} & \\textbf{\$avg(\\p_b)\$} & \\textbf{\$\\sigma(\\p_b)\$}")
            list.forEach { builder.append("\n$it") }
            return builder.toString()
        }
    }

    private fun saveStatistics(statistics: Statistics) {
        files.Utils.saveStringToFile(statistics.toString(), "$folder/${statistics.numRadialNeurons}.partial")
    }

    fun saveTable(table: Table) {
        println("-------------- Whole table ----------------- ")
        files.Utils.saveStringToFile(table.toString(), "$folder/table.whole")
    }

}

fun main() {
    val table = Table()

    table.saveTable(table.buildTable())
}