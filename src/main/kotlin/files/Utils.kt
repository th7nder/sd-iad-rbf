package files

import java.io.FileWriter

class Utils {
    companion object {
        fun saveStringToFile(string: String, filename: String) {
            val writer = FileWriter(filename)
            writer.write(string)
            writer.close()
        }
    }
}