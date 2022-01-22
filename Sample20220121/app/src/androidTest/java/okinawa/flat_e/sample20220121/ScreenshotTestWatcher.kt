package okinawa.flat_e.sample20220121

import org.junit.rules.TestWatcher
import java.io.File
import androidx.test.uiautomator.UiDevice
import android.os.Environment
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry

/**
 * take screenshot when test fails
 *
 * Reference:
 * How to take screenshot at the point where test fail in Espresso?
 * https://stackoverflow.com/questions/38519568/how-to-take-screenshot-at-the-point-where-test-fail-in-espresso
 */
class ScreenshotTestWatcher: TestWatcher() {
    override fun failed(e: Throwable?, description: org.junit.runner.Description?) {
        Log.d(this.javaClass.simpleName,"called")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val path = File(
            appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
                    + "/screenshots/" + appContext.packageName
        )

        if (!path.exists()) {
            path.mkdirs()
        }

        description?.let { desc ->
            val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val filename: String = desc.className.toString() + "-" + desc.methodName + ".png"
            val file = File(path, filename)
            device.takeScreenshot(file)
            Log.d(this.javaClass.simpleName,"take screenshot:$file")
        }
    }
}