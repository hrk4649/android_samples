package okinawa.flat_e.after_shut_down_sample.fragment


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import okinawa.flat_e.after_shut_down_sample.databinding.DialogExampleBinding
import okinawa.flat_e.after_shut_down_sample.viewmodel.ExampleViewModel

/**
 * ダイアログフラグメントの例
 *
 * https://developer.android.com/guide/topics/ui/dialogs?hl=ja#DialogFragment
 */
class ExampleDialogFragment: DialogFragment() {

    // use view binding
    // https://developer.android.com/topic/libraries/view-binding?hl=ja

    private var _binding: DialogExampleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!


    private val model: ExampleViewModel by activityViewModels()

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity ?.let {
//            val builder = ExampleDialogFragment.Builder(it)
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // return super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogExampleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ダイアログのサイズを広げる
        // https://stackoverflow.com/questions/2306503/how-to-make-an-alert-dialog-fill-90-of-screen-size
        val layoutParams = WindowManager.LayoutParams()
        this.dialog?.window?.attributes?.let {
            layoutParams.copyFrom(it)
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        this.dialog?.window?.apply {
            attributes = layoutParams
        }

        model.text1.value?.let {
            binding.editText1.text = SpannableStringBuilder(it)
        }
        binding.editText1.addTextChangedListener { it: Editable? ->
            it?.let {
                model.text1.value = it.toString()
            }
        }

        model.text2.value?.let {
            binding.editText2.text = SpannableStringBuilder(it)
        }
        binding.editText2.addTextChangedListener { it: Editable? ->
            it?.let {
                model.text2.value = it.toString()
            }
        }

        model.textCheck.observe(viewLifecycleOwner) {
            binding.textView4.text = it
        }

        binding.buttonOk.setOnClickListener {
            // ダイアログを閉じる
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}