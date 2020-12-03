package com.supernova.bkashmanager.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.supernova.bkashmanager.databinding.DialogListBinding
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.Utils

class CustomListDialog: DialogFragment(), AdapterView.OnItemClickListener {

    private lateinit var binding: DialogListBinding

    var user: User? = null
    var history: History? = null
    var items: List<String>? = null
    var listType: Int = 0
    var listener: ListItemClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogListBinding.inflate(inflater)
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

        if (context != null && items != null) {

            val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, items!!)

            binding.listView.adapter = adapter
            binding.listView.onItemClickListener = this
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        when (listType) {

            Constants.LIST_TYPE_USER -> listener?.onItemClick(p2, user)
            Constants.LIST_TYPE_HISTORY -> listener?.onItemClick(p2, history)
        }

        dismiss()
    }

    interface ListItemClickListener {

        fun onItemClick(position: Int, user: User?)
        fun onItemClick(position: Int, history: History?)
    }
}