package com.supernova.bkashmanager.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.*
import androidx.fragment.app.DialogFragment
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.DialogInputBinding
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.Utils

class CustomInputDialog : DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogInputBinding

    var dialogId: Int = 0
    var dialogType: Int = 0
    var dialogTitle: String? = ""
    var dialogContent: String? = ""
    var textHint: String? = ""
    var listener: InputListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogInputBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onResume() {

        super.onResume()

        val window = dialog?.window

        val width = Utils.getScreenWidth(activity)
        window?.setLayout((width * 0.90).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER_HORIZONTAL)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initialize() {

        if (!dialogTitle.isNullOrEmpty()) {

            binding.dialogInputTitle.text = dialogTitle
        }

        binding.dialogInputTextBox.setText(dialogContent)
        binding.dialogInputTextBox.hint = textHint

        binding.confirmButton.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)

        when (dialogType) {

            Constants.DIALOG_TYPE_TEXT -> {

                binding.dialogInputTextBox.inputType = InputType.TYPE_CLASS_TEXT
            }

            Constants.DIALOG_TYPE_NUMBER -> {

                binding.dialogInputTextBox.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
            }

            Constants.DIALOG_TYPE_PASSWORD -> {

                binding.dialogInputTextBox.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.dialogInputTextBox.transformationMethod = PasswordTransformationMethod.getInstance()

                binding.dialogInputTextBox2.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.confirm_button -> {

                listener?.onInput(dialogId, binding.dialogInputTextBox.text.toString(), binding.dialogInputTextBox2.text.toString())
                binding.dialogInputTextBox.setText("")
                binding.dialogInputTextBox2.setText("")
                dismiss()
            }

            R.id.cancel_button -> {

                binding.dialogInputTextBox.setText("")
                binding.dialogInputTextBox2.setText("")
                dismiss()
            }
        }
    }

    interface InputListener {

        fun onInput(id: Int, text: String?, text2: String? = "")
    }
}