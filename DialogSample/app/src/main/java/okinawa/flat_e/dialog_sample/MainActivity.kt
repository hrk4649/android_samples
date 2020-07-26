package okinawa.flat_e.dialog_sample

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import okinawa.flat_e.R
import java.lang.IllegalStateException


/**
 * ビューモデル
 */
class MainActivityViewModel: ViewModel() {
    val message = MutableLiveData<String>()
}

/**
 * ダイアログ表示用フラグメント
 */
class MyDialogFragment:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)

        val viewModel: MainActivityViewModel by activityViewModels()

        // ダイアログを生成して返す
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle("title")
                .setMessage("message:" + viewModel.message.value)
                .setCancelable(false)
                .setPositiveButton("ok") { _, _ ->
                    // DialogFragment#dismiss()を呼ぶ
                    // https://stackoverflow.com/questions/11201022/how-to-correctly-dismiss-a-dialogfragment
                    // これをしないと、以下のような現象が発生する
                    // ダイアログのOKボタンを押し、画面を回転させた後に、ダイアログが復活する。
                    // ダイアログのOKボタンを押しても、ダイアログが消えない。
                    this@MyDialogFragment.dismiss()
                }
                .setNegativeButton("cancel") { _, _ ->
                    // cancelボタン押下時も同様
                    this@MyDialogFragment.dismiss()
                }
                .create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // PositiveButtonが押された場合に実行する
    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
        Log.d("MyDialogFragment", "onDismiss")
    }

    override fun onCancel(dialog: DialogInterface) {
//        super.onCancel(dialog)
        Log.d("MyDialogFragment", "onCancel")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyDialogFragment", "onDestroy")
    }
}

/**
 * 画面回転などで、アクティビティを再作成する際に、ダイアログの表示の復元を行う。
 * (参考)
 * https://developer.android.com/guide/topics/ui/dialogs?hl=ja#ShowingADialog
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate")

        val viewModel: MainActivityViewModel by viewModels()

        val editText = findViewById<EditText>(R.id.editText)

        // EditTextの内容をビューモデルに反映する
        editText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.message.value = editText.text?.toString() ?: ""
            }
        })

        val textView = findViewById<TextView>(R.id.textView)

        // ビューモデルの内容をTextView反映する
        viewModel.message.observe(this) { it ->
            textView.text = it
        }

        // ボタンが押されたらダイアログを表示する
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { _ ->
            showDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
    }

    private fun showDialog() {
        val fragment = MyDialogFragment()
        fragment.show(supportFragmentManager,null)
    }
}