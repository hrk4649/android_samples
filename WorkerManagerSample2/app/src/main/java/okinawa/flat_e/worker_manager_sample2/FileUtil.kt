package okinawa.flat_e.worker_manager_sample2

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.nio.charset.Charset
import java.util.*

class FileUtil(val context: Context) {
    companion object {
        private const val DIRNAME = "SampleWorker2"
        private const val FILENAME = "date.txt"
    }

    // https://developer.android.com/training/data-storage/app-specific?hl=ja
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun getAppSpecificStorageDir() =
        File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), DIRNAME)

    fun appendText(txt:String) {
        if (!isExternalStorageWritable()) {
            Log.e(
                "FileUtil.appendText",
                "External storage is not available."
            )
            return
        }

        val dir = getAppSpecificStorageDir()
        if (!dir.mkdirs()) {
            Log.e(
                "FileUtil.appendText",
                "Directory not created"
            )
        }
        val file = File(dir, FILENAME)

        file.appendText("$txt${System.getProperty("line.separator")}", Charset.forName("UTF8"))
    }

    fun removeOldLine() {
        if (!isExternalStorageWritable()) {
            Log.e(
                "FileUtil.removeOldLine",
                "External storage is not available."
            )
            return
        }

        val dir = getAppSpecificStorageDir()
        val file = File(dir, FILENAME)
        val lines = file.readLines(Charset.forName("UTF8"))
        // remove oldest line
        val keepSize = 5
        if (lines.size > keepSize) {
            file.delete()
            lines.filterIndexed {index, _ ->
                index >= (lines.size - keepSize)
            }.forEach {line ->
                Log.d("FileUtil.removeOldLine","append $line")
                file.appendText("$line${System.getProperty("line.separator")}", Charset.forName("UTF8"))
            }
        }
    }

    fun readLines():List<String> {
        if (!isExternalStorageWritable()) {
            Log.e(
                "FileUtil.readLines",
                "External storage is not available."
            )
            return listOf()
        }

        val dir = getAppSpecificStorageDir()
        val file = File(dir, FILENAME)
        val lines = file.readLines(Charset.forName("UTF8"))
        return lines
    }
}