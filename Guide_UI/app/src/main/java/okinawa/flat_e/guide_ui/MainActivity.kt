package okinawa.flat_e.guide_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.constraintlayout.widget.ConstraintLayout
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ConstraintLayout>(R.id.mainLayout)?.setOnClickListener{
            doGuide()
        }
    }

    private fun doGuide() {
        val targets = arrayListOf<Target>()

        val firstRoot = FrameLayout(this)
        val firstOverlay = layoutInflater.inflate(R.layout.layout_target, firstRoot)
        firstOverlay.findViewById<TextView>(R.id.textView2)?.apply {
            text = "This is \"Hello World!\""
        }
        val firstTarget = Target.Builder()
            .setAnchor(findViewById<TextView>(R.id.textView))
            .setShape(RoundedRectangle(200f, 600f, 50f))
            .setOverlay(firstOverlay)
            .build()
        targets.add(firstTarget)

        val secondRoot = FrameLayout(this)
        val secondOverlay = layoutInflater.inflate(R.layout.layout_target_button, secondRoot)
        val secondTarget = Target.Builder()
            .setAnchor(findViewById<TextView>(R.id.button))
            .setShape(Circle(150f))
            .setOverlay(secondOverlay)
            .build()
        targets.add(secondTarget)


        val spotlight = Spotlight.Builder(this)
            .setTargets(targets)
            .setBackgroundColorRes(R.color.spotlightBackground)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .setOnSpotlightListener(object:OnSpotlightListener{
                override fun onStarted() {
                    makeText(
                        this@MainActivity,
                        "spotlight start",
                        LENGTH_SHORT).show()
                }
                override fun onEnded() {
                    makeText(
                        this@MainActivity,
                        "spotlight ended",
                        LENGTH_SHORT).show()
                }
            })
            .build()

        spotlight.start()

        firstOverlay.setOnClickListener {
            spotlight.next()
        }
        secondOverlay.setOnClickListener {
            spotlight.next()
        }
    }
}