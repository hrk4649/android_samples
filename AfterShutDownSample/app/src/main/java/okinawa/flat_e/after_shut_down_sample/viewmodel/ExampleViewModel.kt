package okinawa.flat_e.after_shut_down_sample.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.*

/**
 * ビューモデルの例
 *
 * ViewModel の概要
 * https://developer.android.com/topic/libraries/architecture/viewmodel?hl=ja
 *
 * ViewModel の保存済み状態のモジュール
 * https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate?hl=ja
 */
class ExampleViewModel(private val state: SavedStateHandle): ViewModel() {

    val text1 = MutableLiveData<String>()


    val text2:MutableLiveData<String> = state.getLiveData<String>("text2")


    val textCheck = MediatorLiveData<String>().apply {
        val self = this
        addSource(text1) {_ -> self.setValue("${text1.value ?:""} ${text2.value ?: ""}")}
        addSource(text2) {_ -> self.setValue("${text1.value ?:""} ${text2.value ?: ""}")}
    }

    init {
        // save state
        state.setSavedStateProvider("example") {
            Log.d("ExampleViewModel.init", "save state")
            if (text1.value != null) {
                bundleOf(Pair("text1", text1.value))
            } else {
                Bundle()
            }
        }

        // restore state
        state.get<Bundle>("example")?.let {
            text1.value = it.getString("text1")
        }
    }
}