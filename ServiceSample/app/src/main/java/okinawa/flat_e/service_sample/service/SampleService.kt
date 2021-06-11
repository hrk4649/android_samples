package okinawa.flat_e.service_sample.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.deploygate.sdk.DeployGate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import okinawa.flat_e.service_sample.MainActivity
import okinawa.flat_e.service_sample.R

class SampleService : Service() {
    companion object {
        private const val ONGOING_NOTIFICATION_ID = 1
        // https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest
        private const val LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY
        private const val LOCATION_REQUEST_INTERVAL_MILLISECOND = 30000L
    }

    private val binder = SampleServiceBinder()

    private var fusedLocationClient:FusedLocationProviderClient? = null

    var location: Location? = null
        private set

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            location = locationResult?.lastLocation
            Log.d("SampleService.onLocationResult","location: $location")
            //DeployGate.logDebug("SampleService.onLocationResult:location: $location")
        }
    }

    inner class SampleServiceBinder: Binder() {
        fun get() = this@SampleService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Log.d("SampleService.onStartCommand","called")
        DeployGate.logDebug("SampleService.onStartCommand:called")
        prepareForeground()
        startLocationClient()

        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        //Log.d("SampleService.onCreate","called")
        DeployGate.logDebug("SampleService.onCreate:called")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationClient()

        //Log.d("SampleService.onDestroy","called")
        DeployGate.logDebug("SampleService.onDestroy:called")
    }

    @SuppressLint("MissingPermission")
    private fun startLocationClient() {
        val locationRequest = LocationRequest.create()
            .setPriority(LOCATION_REQUEST_PRIORITY)
            .setInterval(LOCATION_REQUEST_INTERVAL_MILLISECOND)
        fusedLocationClient = FusedLocationProviderClient(this)
        val result = fusedLocationClient?.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper())
    }

    private fun stopLocationClient() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
        fusedLocationClient = null
    }

    private fun prepareForeground() {
        //Log.d("SampleService.prepareForeground","called")
        DeployGate.logDebug("SampleService.prepareForeground:called")
        // https://developer.android.com/guide/components/foreground-services?hl=ja
        // 通知をタップしたときにこのアクテビティを開く
        val pendingIntent: PendingIntent =
            Intent(applicationContext, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)
            }

        // 通知の作成
        // https://qiita.com/naoi/items/03e76d10948fe0d45597#minsdkversion%E3%81%8C26%E4%BB%A5%E4%B8%8A%E3%81%AE%E5%A0%B4%E5%90%88
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 通知チャンネルの作成
        // https://developer.android.com/guide/topics/ui/notifiers/notifications?hl=ja#ManageChannels
        val name = getText(R.string.notification_title)
        val channelId = getString(R.string.app_name)
        val notifyDescription = getString(R.string.notification_message)

        if (manager.getNotificationChannel(channelId) == null) {
            val mChannel = NotificationChannel(
                channelId,
                name,
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(channelId)
            .setContentText(notifyDescription)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            // 5.0 以降 ティッカーは設定しても表示されない
            // https://phicdy.hatenablog.com/entry/android-notification-differences
            //.setTicker(getText(R.string.ticker_text))
            .build()

        // Notification ID cannot be 0.
        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }
}