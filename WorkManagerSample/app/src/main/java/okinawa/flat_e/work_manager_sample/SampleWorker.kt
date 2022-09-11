package okinawa.flat_e.work_manager_sample

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.deploygate.sdk.DeployGate
import com.google.android.gms.location.Priority
import java.text.SimpleDateFormat
import java.util.*

/**
 * サービスに接続し、通知を変更するワーカー
 */
class SampleWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    private var serviceConnection: SampleServiceConnection? = SampleServiceConnection()

    companion object {
        private const val NOTIFICATION_ID = 2
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification(
                applicationContext.getString(R.string.worker)
            )
        )
    }

    override suspend fun doWork(): Result {
        Log.d("SampleWorker.doWork","called")

        // サービスに接続する
        val bindIntent = Intent(applicationContext, SampleService::class.java)
        val bindResult = serviceConnection?.let { serviceConnection ->
            applicationContext.bindService(
                bindIntent,
                serviceConnection,
                0 // AppCompatActivity.BIND_AUTO_CREATE
            )
        } ?: false
        // TODO サービスが取得できない場合があるのを解消する
        val service = serviceConnection?.mService
        Log.d("SampleWorker.doWork","bindResult: $bindResult service: $service")
        service?.let {
            val date = Date()
            val format = SimpleDateFormat(
                applicationContext.getString(R.string.date_format)
            )
            val txt = "From Worker:${format.format(date)}"
            it.showMessageAsNotification(txt)
            Log.d("SampleWorker.doWork","Notification: $txt")
            // applicationContext.unbindService(serviceConnection)
        }
        return Result.success()
    }

    private fun createNotification(contentText: String) : Notification {
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
}