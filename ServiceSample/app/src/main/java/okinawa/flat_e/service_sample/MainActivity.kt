package okinawa.flat_e.service_sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.deploygate.sdk.DeployGate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okinawa.flat_e.service_sample.service.SampleService
import okinawa.flat_e.service_sample.service.SampleServiceConnection
import java.util.*

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