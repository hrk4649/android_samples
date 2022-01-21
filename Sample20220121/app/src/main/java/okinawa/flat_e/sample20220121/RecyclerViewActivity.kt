package okinawa.flat_e.sample20220121

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * callback when an item is clicked
     */
    var clickCallback: ((String) -> Unit)? = {s:String ->}

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]

        // pass the value to the callback
        viewHolder.itemView.setOnClickListener { _ ->
            clickCallback?.let {
                it.invoke(dataSet[position])
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}


class RecyclerViewActivity : AppCompatActivity() {
    lateinit var adapter:CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        var array = arrayOf<String>()
        for (i in 0 until 20) {
            array = array.plus(i.toString())
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        adapter = CustomAdapter(array)
        adapter.clickCallback = { s ->
            val textView = findViewById<TextView>(R.id.textView1)
            textView.text = s
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.apply {
            adapter.clickCallback = null
        }
    }
}