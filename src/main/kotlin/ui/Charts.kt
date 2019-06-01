package utils


import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart


class Charts {
    companion object {
        fun saveChart(file: String, chart : XYChart) {
            BitmapEncoder.saveBitmap(
                chart,
                "data/$file",
                BitmapEncoder.BitmapFormat.PNG
            )
        }

    }
}