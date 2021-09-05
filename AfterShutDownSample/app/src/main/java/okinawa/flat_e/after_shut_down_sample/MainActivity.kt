package okinawa.flat_e.after_shut_down_sample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import okinawa.flat_e.after_shut_down_sample.databinding.ActivityMainBinding
import okinawa.flat_e.after_shut_down_sample.fragment.ExampleDialogFragment
import okinawa.flat_e.after_shut_down_sample.viewmodel.ExampleViewModel

class MainActivity : AppCompatActivity() {

    // use binding
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
    }

    private fun initView() {

        val viewModel: ExampleViewModel by viewModels()
        viewModel.textCheck.observe(this){
            binding.textView1.text = it
        }

        binding.button1.setOnClickListener {
            val fragment = ExampleDialogFragment()

            fragment.show(supportFragmentManager, "example")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("MainActivity.onSaveInstanceState", "called. isFinishing $isFinishing")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity.onDestroy", "called. isFinishing $isFinishing")

        // accessing to a view model when "isFinishing = false" will cause
        // "java.lang.IllegalArgumentException: SavedStateProvider with the given key is already registered"

        // val viewModel: ExampleViewModel by viewModels()
        // Log.d("MainActivity.onDestroy", "viewModel.textCheck ${viewModel.textCheck}")
    }
}