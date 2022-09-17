package okinawa.flat_e.worker_manager_sample2

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class SampleWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val NOTIFICATION_ID = 2
        private const val DIRNAME = "SampleWorker2"
        private const val FILENAME = "date.txt"
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification(
                applicationContext.getString(R.string.worker)
            )
        )
    }

    private fun createNotification(contentText: String): Notification {
        val channelId = applicationContext.getString(R.string.app_name)

        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(channelId)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            // 5.0 以降 ティッカーは設定しても表示されない
            // https://phicdy.hatenablog.com/entry/android-notification-differences
            //.setTicker(getText(R.string.ticker_text))
            .build()
    }

    override suspend fun doWork(): Result {
        Log.d("SampleWorker.doWork", "called")
        val result = runCatching {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val txt = "${format.format(Date())}"
            FileUtil(applicationContext).appendText(txt)
        }

        return if (result.isSuccess) {
            Result.success()
        } else {
            Log.e("SampleWorker.doWork", "error", result.exceptionOrNull())
            Result.failure()
        }
    }
}
