package okinawa.flat_e.sample20220121

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener { v ->
            Log.d("onCreate","button1 clicked")
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener { v ->
            Log.d("onCreate","button2 clicked")
            startActivity(Intent(this, NumberPickerActivity::class.java))
        }
    }
}