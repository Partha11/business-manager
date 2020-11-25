package com.supernova.bkashmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.ModelUserBinding
import com.supernova.bkashmanager.model.User

class UserAdapter(private val context: Context?): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var users: List<User>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = users?.get(position)

        if (user != null) {

            holder.binding.userName.text = user.userName
            holder.binding.userEmail.text = context?.getString(R.string.user_email, user.userEmail)
            holder.binding.userNumber.text = context?.getString(R.string.user_number, user.userNumber)
            holder.binding.userBalance.text = context?.getString(R.string.user_balance, user.currentPoints)

            if (context != null) {

                if (user.isBanned) {

                    holder.binding.userStatus.setTextColor(ContextCompat.getColor(context, R.color.blocked_red))
                    holder.binding.userStatus.text = context.getString(R.string.user_status, "Banned")

                } else {

                    holder.binding.userStatus.setTextColor(ContextCompat.getColor(context, R.color.active_green))
                    holder.binding.userStatus.text = context.getString(R.string.user_status, "Active")
                }
            }
        }
    }

    override fun getItemCount(): Int {

        return users?.size ?: 0
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding: ModelUserBinding = ModelUserBinding.bind(view)
    }
}