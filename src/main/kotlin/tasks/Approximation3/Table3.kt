package tasks.Approximation3

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import math.Utils
import network.sigmas.EqualSigmaGenerator

class Table3 : Approximation3() {
    fun singleRow(numRadialNeurons: Int) : Statistics {
        val asyncs = (1..tableIters).map { GlobalScope.async { singleNetwork(numRadialNeurons, EqualSigmaGenerator())} }

        val networks = runBlocking {
            asyncs.map { it.await() }
        }

        val trainingErrors = networks.map { it.error() }
        val testErrors =  networks.map { it.error(testData) }

        return Statistics (
            numRadialNeurons,
            trainingErrors.average(),
            Utils.standardDeviation(trainingErrors),
            testErrors.average(),
            Utils.standardDeviation(testErrors)
        )
    }

    data class Statistics(
        val numRadialNeurons: Int,
        val trainingAverageError : Double,
        val trainingStandardDeviation: Double,
        val testAverageError : Double,
        val testStandardDeviation: Double
    ) {
        override fun toString(): String {
            return "$numRadialNeurons & $trainingAverageError & $trainingStandardDeviation & $testAverageError & $testStandardDeviation \\\\"
        }
    }

    class Table(val list: List<Statistics>) {
        override fun toString(): String {
            val builder = StringBuilder()
            builder.append("\\textbf{K} & \\textbf{\$avg(\\epsilon_a)\$} & \\textbf{\$\\sigma(\\epsilon_a)\$} & \\textbf{\$avg(\\epsilon_b)\$} & \\textbf{\$\\sigma(\\epsilon_b)\$}")
            list.forEach { builder.append("\n$it") }
            return builder.toString()
        }
    }

    fun buildTable() : Table {
        val statistics = (1..41 step 5).map { singleRow(it) }
        return Table(statistics)
    }

    override fun getTrainingIterations() = 1000
    override fun getDisplayIterations() = 1000
    val tableIters = 100
}

fun main() {
    val table3 = Table3()
    println(table3.buildTable())
}