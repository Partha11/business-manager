package com.supernova.bkashmanager.model

import android.graphics.drawable.Drawable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SettingsItem (
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("content")
    var content: String,
    @SerializedName("thumbIcon")
    var thumbIcon: Drawable?,
    @SerializedName("icon")
    var thumbIconString: String,
    @SerializedName("is_title")
    var isTitle: Boolean
)