package okinawa.flat_e.sample20220121

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.test.espresso.FailureHandler
import androidx.test.espresso.base.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.hamcrest.Matcher
import java.io.File
import java.lang.Exception
import java.util.*

/**
 * FailureHandler that can take screenshot, which is stored on the device.
 *
 * Reference:
 * How to take screenshot at the point where test fail in Espresso?
 * https://stackoverflow.com/questions/38519568/how-to-take-screenshot-at-the-point-where-test-fail-in-espresso
 */
class MyFailureHandler(
    appContext: Context
) : FailureHandler {

    var defaultFailureHandler: DefaultFailureHandler = DefaultFailureHandler(appContext)

    override fun handle(error: Throwable?, viewMatcher: Matcher<View>?) {
        try {
            takeScreenshot()
        } catch (e:Exception) {
            Log.e(this.javaClass.simpleName,"error", e)
        }

        defaultFailureHandler.handle(error, viewMatcher)
    }

    private fun takeScreenshot() {
        Log.d(this.javaClass.simpleName,"called")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val path = File(
            appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
                    + "/screenshots/"
        )

        if (!path.exists()) {
            path.mkdirs()
        }

        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val filename: String = Date().toString() + ".png"
        val file = File(path, filename)
        device.takeScreenshot(file)
        Log.d(this.javaClass.simpleName,"take screenshot:$file")
    }
}