package com.supernova.bkashmanager.listener

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.User

interface FragmentInteractionListener {

    fun getManagerName(): String
    fun getInitialPoints(): Int
    fun getCurrentPoints(): Int

    fun getStorageListener(): LiveData<Pair<String, String>>
    fun getUsers(): LiveData<List<User>>
    fun getHistories(date: String): LiveData<List<History>>
}