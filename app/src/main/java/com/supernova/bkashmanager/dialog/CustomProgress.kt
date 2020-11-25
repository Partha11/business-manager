package com.supernova.bkashmanager.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.supernova.bkashmanager.databinding.DialogProgressBinding
import com.supernova.bkashmanager.util.Utils

class CustomProgress : DialogFragment() {

    private lateinit var binding: DialogProgressBinding

    var dialogTitle: String? = ""
    var dialogContent: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DialogProgressBinding.inflate(inflater)
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

        binding.dialogProgressTitle.text = dialogTitle
        binding.dialogProgressMessage.text = dialogContent
    }
}
