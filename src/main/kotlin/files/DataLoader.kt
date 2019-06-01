package files

import math.Point
import java.io.File

class DataLoader {
    companion object {
        fun loadFile(filename: String, numInput: Int, numOutput: Int): ArrayList<Pair<Point, Point>> {
            val data = ArrayList<Pair<Point, Point>>()
            File("data/inputs/$filename").forEachLine {
                val numbers = it.split(" ").map(String::toDouble)
                data += Pair(
                    Point(numbers.take(numInput)),
                    Point(numbers.takeLast(numOutput))
                )
            }

            return data
        }
    }
}