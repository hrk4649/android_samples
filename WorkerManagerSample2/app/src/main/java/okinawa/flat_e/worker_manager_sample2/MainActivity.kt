package okinawa.flat_e.worker_manager_sample2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWorkManager()
    }

    override fun onResume() {
        super.onResume()
        initTextView()
    }

    private fun initTextView() {
        runCatching {
            val textView = findViewById<TextView>(R.id.textView)
            val fileUtil = FileUtil(this)
            fileUtil.removeOldLine()
            val lines = fileUtil.readLines()
            // TextView で改行する
            val txt = lines.joinToString('\n'.toString())
            Log.d("MainActivity.initTextView","txt:$txt")
            textView.text = txt
        }.onFailure {
            Log.e("MainActivity.initTextView","error", it)
        }
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
}