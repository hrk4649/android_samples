package okinawa.flat_e.worker_manager_sample2

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.nio.charset.Charset

/**
 * 外部ストレージにファイルを書き込む処理を管理するユーティリティクラス
 *
 * 参考
 * アプリ固有のファイルにアクセスする
 * https://developer.android.com/training/data-storage/app-specific?hl=ja
 */
class FileUtil(private val context: Context) {
    companion object {
        private const val DIRNAME = "SampleWorker2"
        private const val FILENAME = "date.txt"

        /** ファイルに保存する行数 */
        private const val KEEP_LINE = 5
    }

    /**
     * 外部ストレージにファイルの書き込みが可能をチェックする
     */
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 外部ストレージのディレクトリ
     */
    private fun getAppSpecificStorageDir() =
        File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), DIRNAME)

    /**
     * ファイルに文字列を書き込みます。
     */
    fun appendText(txt: String) {
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

    /**
     * ファイルに書き込まれた行のうち、古い行を削除します。
     */
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
        if (lines.size > KEEP_LINE) {
            file.delete()
            lines.filterIndexed { index, _ ->
                index >= (lines.size - KEEP_LINE)
            }.forEach { line ->
                Log.d("FileUtil.removeOldLine", "append $line")
                file.appendText(
                    "$line${System.getProperty("line.separator")}",
                    Charset.forName("UTF8")
                )
            }
        }
    }

    /**
     * ファイルから行を読み込みます。
     */
    fun readLines(): List<String> {
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