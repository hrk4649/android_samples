package okinawa.flat_e.work_manager_sample

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class SampleServiceConnection: ServiceConnection {

    var mService :SampleService? = null
    var mBound:Boolean = false
        private set

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as SampleService.SampleServiceBinder
        mService = binder.get()
        mBound = true
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mService = null
        mBound = false
    }
}