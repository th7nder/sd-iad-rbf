package network.sigmas

import math.Point

interface SigmaGenerator {
    fun generate(centers: List<Point>) : List<Double>
}