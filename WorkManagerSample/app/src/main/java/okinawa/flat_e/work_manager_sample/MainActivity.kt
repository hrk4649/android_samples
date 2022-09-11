package okinawa.flat_e.work_manager_sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.deploygate.sdk.DeployGate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var serviceConnection: SampleServiceConnection? = SampleServiceConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Log.d("MainActivity.onCreate", "called")
        DeployGate.logDebug("MainActivity.onCreate:called")

        startSampleService()

        val textView1 = findViewById<TextView>(R.id.textView1)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                while (true) {
                    delay(30000)
                    val location = serviceConnection?.mService?.location
                    location?.let {
                        val date = Date(location.time)
                        val txt = "$date (${location.latitude}, ${location.longitude})"
                        runOnUiThread {
                            textView1.text = txt
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity.onCreate", "error:", e)
            }
        }

        initWorkManager()
    }

    private fun initWorkManager() {
        lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                // ワーカーの起動をずらす
                delay(10000)
                // 最低 15 分間隔
                // https://developer.android.com/topic/libraries/architecture/workmanager/how-to/define-work?hl=ja#schedule_periodic_work
                val workRequest = PeriodicWorkRequestBuilder<SampleWorker>(
                    15, TimeUnit.MINUTES
                ).build()

                val workManager = WorkManager.getInstance(applicationContext)
                workManager.enqueueUniquePeriodicWork(
                    getString(R.string.worker)
                    , ExistingPeriodicWorkPolicy.REPLACE
                    , workRequest
                )
                Log.d("MainActivity.initWorkManager","start worker")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Log.d("MainActivity.onDestroy", "called")
        DeployGate.logDebug("MainActivity.onDestroy:called")
        stopSampleService()

        serviceConnection = null
    }

    private fun startSampleService() {
        //Log.d("MainActivity.startSampleService", "start Service")
        DeployGate.logDebug("MainActivity.startSampleService:start Service")
        val serviceIntent = Intent(this.application, SampleService::class.java)
        startForegroundService(serviceIntent)

        val bindIntent = Intent(this, SampleService::class.java)
        serviceConnection?.let { serviceConnection ->
            //Log.d("MainActivity.startSampleService", "bind Service")
            DeployGate.logDebug("MainActivity.startSampleService:bind Service")
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE)
        }
    }

    private fun stopSampleService() {
        if (serviceConnection?.mBound == true) {
            //Log.d("MainActivity.stopSampleService", "unbind Service")
            DeployGate.logDebug("MainActivity.stopSampleService:unbind Service")
            unbindService(serviceConnection!!)
        }

        val serviceIntent = Intent(this.application, SampleService::class.java)
        //Log.d("MainActivity.stopSampleService", "stop service")
        DeployGate.logDebug("MainActivity.stopSampleService:stop service")
        stopService(serviceIntent)
    }
}