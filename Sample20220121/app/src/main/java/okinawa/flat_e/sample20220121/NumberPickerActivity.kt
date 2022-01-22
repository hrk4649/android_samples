package okinawa.flat_e.sample20220121

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker

class NumberPickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_picker)

        findViewById<NumberPicker>(R.id.numberPicker1)?.apply {
            minValue = 0
            maxValue = 9
            value = 0
        }
        findViewById<NumberPicker>(R.id.numberPicker2)?.apply {
            minValue = 0
            maxValue = 9
            value = 0
        }
        findViewById<NumberPicker>(R.id.numberPicker3)?.apply {
            minValue = 0
            maxValue = 9
            value = 0
        }

    }
}