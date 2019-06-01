package network.sigmas

import math.Point
import kotlin.math.sqrt

open class EqualSigmaGenerator : SigmaGenerator {
    override fun generate(centers: List<Point>): List<Double> {
        if (centers.size == 1) {
            return arrayListOf(1.0)
        }

        val distances = ArrayList<Double>()
        for (center in centers) {
            for (anotherCenter in centers) {
                distances += center.distance(anotherCenter)
            }
        }

        val maximumDistance = distances.max()!!
        val sigma = maximumDistance / sqrt(2.0 * centers.size)
        return centers.map { sigma }
    }
}