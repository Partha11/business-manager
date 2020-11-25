package com.supernova.bkashmanager.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.DialogPickerBinding
import com.supernova.bkashmanager.listener.DateChangeListener
import com.supernova.bkashmanager.util.Utils


class CustomPicker: DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogPickerBinding

    var selectedDate: String = ""
    var listener: DateChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogPickerBinding.inflate(inflater)
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
        window?.setLayout((width * 0.88).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER_HORIZONTAL)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initialize() {

        if (context != null) {

            val typeface = ResourcesCompat.getFont(context!!, R.font.open_sans)

            if (typeface != null) {

                binding.calendar.setFonts(typeface)
            }

            binding.calendar
        }

//        var year = 0
//        var month = 0
//        var day = 0
//
//        if (selectedDate.isNotEmpty()) {
//
//            val tmp = selectedDate.split("-")
//
//            year = tmp[0].toInt()
//            month = tmp[1].toInt() - 1
//            day = tmp[2].toInt()
//
//        } else {
//
//            val calendar = Calendar.getInstance()
//
//            year = calendar.get(Calendar.YEAR)
//            month = calendar.get(Calendar.MONTH)
//            day = calendar.get(Calendar.DAY_OF_MONTH)
//        }
//
//        binding.datePicker.init(year, month, day) { _, y, m, d ->
//
//            val monthString = Utils.appendLeadingZero(m + 1)
//            val dayString = Utils.appendLeadingZero(d)
//
//            selectedDate = "$y-$monthString-$dayString"
//        }
//
//        binding.confirmButton.setOnClickListener(this)
//        binding.cancelButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        if (view?.id == R.id.confirm_button) {

            Log.d("Confirm Clicked", selectedDate)
            listener?.onDateChanged(selectedDate)
        }

        dismiss()
    }
}