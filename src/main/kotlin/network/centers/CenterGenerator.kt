package network.centers

import math.Point

interface CenterGenerator {
    fun generate(numCenters : Int, data: List<Point>) : List<Point>
}