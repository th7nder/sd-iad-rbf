package network.centers.selforganizing

import math.Point
import network.centers.selforganizing.common.Assigner
import network.centers.selforganizing.common.Centroid


abstract class Algorithm(val name: String) {
    private val assigner = Assigner()
    var data : List<Point> = ArrayList()
    var centroids : List<Centroid> = ArrayList()

    fun setTrainingData(data: List<Point>, centroids: List<Centroid>) {
        this.data = data
        this.centroids = centroids
        assign()
    }

    protected fun assign() {
        centroids.forEach(Centroid::clear)
        assigner.assign(data, centroids)
    }

    fun error() = centroids.sumByDouble { it.sumDistanceToPoints() } / data.size
    fun getInactiveNeurons() = centroids.count { it.points.size == 0 }

    abstract fun step(iteration: Int = 0)

    fun train(iters: Int,  onIteration : ((i : Int, error: Double) -> Unit)? = null) {
        onIteration?.invoke(0, error())
        for (i in 1..iters) {
            step()
            onIteration?.invoke(i, error())
        }
    }

}