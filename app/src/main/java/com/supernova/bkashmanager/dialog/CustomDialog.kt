package com.supernova.bkashmanager.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.DialogCustomBinding
import com.supernova.bkashmanager.listener.DialogInteractionListener
import com.supernova.bkashmanager.util.Utils

class CustomDialog : DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogCustomBinding

    var dialogId: Int = 0
    var dialogTitle: String? = ""
    var dialogContent: String? = ""
    var isConfirmVisible = false
    var listener: DialogInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DialogCustomBinding.inflate(inflater)
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

        binding.dialogCustomTitle.text = dialogTitle
        binding.dialogCustomMessage.text = dialogContent
        binding.confirmButton.visibility = if (isConfirmVisible) View.VISIBLE else View.GONE

        binding.confirmButton.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.confirm_button -> {

                listener?.onConfirm(dialogId)
                dismiss()
            }

            R.id.cancel_button -> dismiss()
        }
    }
}
