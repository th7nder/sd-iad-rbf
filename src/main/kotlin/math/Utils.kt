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
    }
}