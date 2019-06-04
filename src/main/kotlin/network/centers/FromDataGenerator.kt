package network.centers

import math.Point

open class FromDataGenerator : CenterGenerator {
    override fun generate(numCenters: Int, data: List<Point>): List<Point> = data.shuffled().take(numCenters).map { it.clone() }
}