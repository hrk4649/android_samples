package okinawa.flat_e.ui_flow_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import okinawa.flat_e.ui_flow_sample.databinding.ActivityMainBinding
import okinawa.flat_e.ui_flow_sample.databinding.FlowItem2Binding
import okinawa.flat_e.ui_flow_sample.databinding.FlowItemBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        initView()
        setContentView(view)
    }

    private fun initView() {
        // ConstraintLayout FlowでViewを動的に追加してみる
        // https://qiita.com/zzt-osamuhanzawa/items/e2b67320000d0b7a9513
        val data = (1..100).map {v -> "DATA $v"}
        data.forEachIndexed{ idx, str->
            val itemView = if (idx % 2 == 0) {
                val v = FlowItemBinding.inflate(layoutInflater)
                v.root.id = View.generateViewId()
                v.flowItem.text = str
                v.root
            } else {
                val v = FlowItem2Binding.inflate(layoutInflater)
                v.root.id = View.generateViewId()
                v.flowItem.text = str
                v.root
            }
            binding.constraintLayout1.addView(itemView)
            binding.flow1.addView(itemView)
        }
    }
}