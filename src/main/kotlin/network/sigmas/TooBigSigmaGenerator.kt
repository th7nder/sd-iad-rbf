package network.sigmas

import math.Point

class TooBigSigmaGenerator : EqualSigmaGenerator() {
    override fun generate(centers: List<Point>): List<Double> {
        return super.generate(centers).map { 5.0 }
    }
}