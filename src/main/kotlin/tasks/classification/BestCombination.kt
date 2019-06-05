package tasks.classification

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.markers.SeriesMarkers
import utils.Charts

class BestCombination : Classification() {
    var folder = "classification4/1"

    fun singleCombination(combination: Combination) {
        val trainingData = projectData(trainingData, combination)
        val testData = projectData(testData, combination)

        val asyncs = (1..41 step 10).map { numRadialNeurons ->
            GlobalScope.async {
                val percentages = Pair(ArrayList<Double>(), ArrayList<Double>())

                val network = createNetwork(numRadialNeurons, trainingData)
                network.train(getTrainingIterations()) { i, error ->
                    percentages.first.add(percentage(trainingData, network))
                    percentages.second.add(percentage(testData, network))

                    if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error")
                }

                Pair(numRadialNeurons, percentages)
            }
        }


        val percentages = runBlocking {
            asyncs.map { it.await() }
        }

        val selectedInputs = combination.second.joinToString(",")
        Charts.saveChart(
            "$folder/${combination.first}_$selectedInputs",
            plotPercentages("Wejścia $selectedInputs", percentages)
        )
    }

    private fun plotPercentages(title: String, percentages: List<Pair<Int, Pair<List<Double>, List<Double>>>>) : XYChart {
        val chart =
            XYChartBuilder()
                .width(800)
                .height(800)
                .yAxisTitle("Skuteczność klasyfikacji")
                .xAxisTitle("Liczba iteracji")
                .title(title).build()

        val skipIters = getTrainingIterations() - 100

        for (data in percentages) {
            val seriesName = "K=${data.first}"

            val trainingSeries = chart.addSeries("$seriesName trening", data.second.first.takeLast(skipIters))
            trainingSeries.marker = SeriesMarkers.NONE

            val testSeries = chart.addSeries("$seriesName testowe", data.second.second.takeLast(skipIters))
            testSeries.marker = SeriesMarkers.NONE

        }
        return chart
    }

    override fun getTrainingIterations(): Int = 5000
    override fun getDisplayIterations(): Int = 1000
}

fun main() {
    val bestCombination = BestCombination()

    for (combination in bestCombination.combinations) {
        bestCombination.singleCombination(combination)
    }
}