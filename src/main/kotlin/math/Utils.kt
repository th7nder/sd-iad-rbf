package math

class Utils {
    companion object {
        fun arange(from : Double, to: Double, step: Double) : List<Double> {
            val range = ArrayList<Double>()
            var value = from

            while (value <= to) {
                range.add(value)
                value += step
            }

            return range
        }

        fun standardDeviation(numArray: List<Double>): Double {
            val mean = numArray.average()
            val sd = numArray.fold(0.0, { accumulator, next -> accumulator + Math.pow(next - mean, 2.0) })
            return Math.sqrt(sd / numArray.size)
        }

        fun argmax(arr: DoubleArray) : Int {
            var maxIndex = 0
            for (index in arr.indices) {
                if (arr[index] > arr[maxIndex]) {
                    maxIndex = index
                }
            }

            return maxIndex
        }
    }
}