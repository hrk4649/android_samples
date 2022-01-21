package okinawa.flat_e.merge_manifest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView1 = findViewById<TextView>(R.id.textView1)
        textView1.text = BuildConfig.APPLICATION_ID

        CheckPermission.checkPermission(applicationContext)
    }
}