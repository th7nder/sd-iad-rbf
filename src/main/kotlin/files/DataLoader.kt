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

        fun loadCSV(filename: String, numInput: Int, numOutput: Int): ArrayList<Pair<Point, Point>> {
            val data = ArrayList<Pair<Point, Point>>()
            File("data/inputs/$filename").forEachLine {
                val numbers = it.trim().split(";")
                    .slice(arrayListOf(1, 3, 4, 5, 6))
                    .map(String::toDouble)
                    .toMutableList()

                data += Pair(
                    Point(numbers.take(numInput)),
                    Point(numbers.takeLast(numOutput))
                )
            }

            return data
        }

        fun loadFile(filename: String, numInput: Int, outputParser: (value : Int) -> Point): ArrayList<Pair<Point, Point>> {
            val data = ArrayList<Pair<Point, Point>>()
            File("data/inputs/$filename").forEachLine {
                val numbers = it.split(" ").map(String::toDouble)
                if (numbers.size - numInput != 1) throw IllegalStateException("File not viable for parsing last number")
                data += Pair(
                    Point(numbers.take(numInput)),
                    outputParser(numbers.last().toInt())
                )
            }

            return data
        }
    }
}