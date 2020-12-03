package com.supernova.bkashmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.ModelSettingsItemBinding
import com.supernova.bkashmanager.databinding.ModelSettingsTitleBinding
import com.supernova.bkashmanager.model.SettingsItem
import com.supernova.bkashmanager.util.Constants

class SettingsAdapter(context: Context): RecyclerView.Adapter<SettingsAdapter.BaseViewHolder>() {

    var items: List<SettingsItem>? = null
    private val listener: OnClickListener = context as OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            Constants.VIEW_SECTION_ITEM -> {
                val view = inflater.inflate(R.layout.model_settings_item, parent, false)
                ItemViewHolder(view)
            }
            Constants.VIEW_SECTION_TITLE -> {
                val view = inflater.inflate(R.layout.model_settings_title, parent, false)
                TitleViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.model_settings_item, parent, false)
                ItemViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {

        return items?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {

        return items?.get(position)?.viewType ?: Constants.VIEW_SECTION_ITEM
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        if (holder is TitleViewHolder) {

            holder.binding.settingsTitle.text = items?.get(position)?.title

        } else if (holder is ItemViewHolder) {

            holder.binding.settingsItemTitle.text = items?.get(position)?.title
            holder.binding.settingsItemContent.text = items?.get(position)?.content

            if (items?.get(position)?.thumbIcon != null) {

                holder.binding.settingsThumb.setImageDrawable(items?.get(position)?.thumbIcon)
            }
        }
    }

    open inner class BaseViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        override fun onClick(v: View?) {

            listener.onClick(items?.get(adapterPosition)?.id ?: 0)
        }
    }

    inner class TitleViewHolder(view: View): BaseViewHolder(view) {

        val binding: ModelSettingsTitleBinding = ModelSettingsTitleBinding.bind(view)
    }

    inner class ItemViewHolder(view: View): BaseViewHolder(view) {

        val binding: ModelSettingsItemBinding = ModelSettingsItemBinding.bind(view)

        init {

            binding.root.setOnClickListener(this)
        }
    }

    interface OnClickListener {

        fun onClick(id: Int)
    }
}