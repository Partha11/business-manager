package com.supernova.bkashmanager.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.DialogUserBinding
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.util.Utils

class CustomUserDialog : DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogUserBinding
    var listener: OnCreateListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogUserBinding.inflate(inflater)
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

        binding.confirmButton.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.confirm_button -> {

                when {

                    binding.dialogUserName.text.toString().isEmpty() -> binding.dialogUserName.error = "Required"
                    binding.dialogUserEmail.text.toString().isEmpty() -> binding.dialogUserEmail.error = "Required"
                    binding.dialogUserPassword.text.toString().isEmpty() -> binding.dialogUserPassword.error = "Required"
                    binding.dialogUserPassword.text.toString().length < 6 -> binding.dialogUserPassword.error = "Passwords should be at least 6 digit long"
                    binding.dialogUserNumber.text.toString().isEmpty() -> binding.dialogUserNumber.error = "Required"

                    else -> {

                        val user = User()

                        user.userName = binding.dialogUserName.text.toString()
                        user.userEmail = binding.dialogUserEmail.text.toString()
                        user.userPassword = binding.dialogUserPassword.text.toString()
                        user.userNumber = binding.dialogUserNumber.text.toString()

                        listener?.onNewUserCreated(user)
                        dismiss()
                    }
                }
            }

            R.id.cancel_button -> dismiss()
        }
    }

    interface OnCreateListener {

        fun onNewUserCreated(user: User)
    }
}
