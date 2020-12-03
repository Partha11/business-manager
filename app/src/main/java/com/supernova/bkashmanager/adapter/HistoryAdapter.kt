package com.supernova.bkashmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.ModelHistoryBinding
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.util.Utils

class HistoryAdapter(private val context: Context?): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var histories: List<History>? = null
    var listener: OnClickListener? = if (context is OnClickListener) context else null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val history = histories?.get(position)

        if (history != null) {

            val type = if (history.transactionMedium != -1) "${history.transactionMediumText} (${history.transactionTypeText})" else history.transactionTypeText

            holder.binding.historyId.text = context?.getString(R.string.request_no, history.id)
            holder.binding.phoneNumber.text = context?.getString(R.string.user_number, history.phoneNumber)
            holder.binding.transactionAmount.text = context?.getString(R.string.trx_amount, history.transactionAmount)
            holder.binding.transactionType.text = context?.getString(R.string.trx_type, type)
            holder.binding.requestTime.text = context?.getString(R.string.request_time, Utils.getLocalTimeFromServerTime(history.requestTime ?: ""))

            if (context != null) {

                if (history.completionFlag == 0) {

                    holder.binding.historyStatus.setTextColor(ContextCompat.getColor(context, R.color.blocked_red))
                    holder.binding.historyStatus.text = context.getString(R.string.user_status, "Pending")
                    holder.binding.completionTime.text = context.getString(R.string.completion_time, "Incomplete")

                } else {

                    holder.binding.historyStatus.setTextColor(ContextCompat.getColor(context, R.color.active_green))
                    holder.binding.historyStatus.text = context.getString(R.string.user_status, "Complete")
                    holder.binding.completionTime.text = context.getString(R.string.completion_time, Utils.getLocalTimeFromServerTime(history.completionTime ?: ""))
                }
            }
        }
    }

    override fun getItemCount(): Int {

        return histories?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val binding: ModelHistoryBinding = ModelHistoryBinding.bind(view)

        init {

            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View?) {

            listener?.onClick(histories?.get(adapterPosition))
        }
    }

    public interface OnClickListener {

        fun onClick(history: History?)
    }
}