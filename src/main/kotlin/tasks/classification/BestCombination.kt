package tasks.classification

class BestCombination : Classification() {
    var folder = "classification4/1"

    fun singleCombination(combination: Combination) {
        val trainingData = projectData(trainingData, combination)
        val testData = projectData(testData, combination)

        (1..41 step 10).map {numRadialNeurons ->
            val network = createNetwork(numRadialNeurons, trainingData)
            network.train(getTrainingIterations()) { i, error ->

                if (i % getDisplayIterations() == 0) println("Radial neurons: $numRadialNeurons | iteration: $i | error: $error")
            }
        }
    }

    override fun getTrainingIterations(): Int = 5000
}

fun main() {
    val bestCombination = BestCombination()

    bestCombination.singleCombination(bestCombination.combinations.last())
}