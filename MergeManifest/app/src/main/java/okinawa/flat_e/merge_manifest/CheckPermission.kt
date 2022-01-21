package okinawa.flat_e.merge_manifest

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat.checkSelfPermission

class CheckPermission {
    companion object {
        fun checkPermission(context: Context) {
            // https://codechacha.com/ja/android-check-permission/
            listOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE
            ).forEach { e ->
                val result = when(checkSelfPermission(context,e)) {
                    0 -> "GRANTED"
                    else -> "DENIED"
                }
                Log.d("checkPermission", "$e $result")
            }
        }
    }
}