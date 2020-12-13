package com.supernova.bkashmanager.listener

import androidx.lifecycle.LiveData
import com.supernova.bkashmanager.model.SettingsItem
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.model.UserHistory

interface FragmentInteractionListener {

    fun getManagerName(): String
    fun getInitialPoints(): Int
    fun getCurrentPoints(): Int

    fun getStorageListener(): LiveData<Pair<String, String>>
    fun getUsers(): LiveData<List<User>>
    fun getHistories(date: String): LiveData<List<UserHistory>>
    fun getItems(): LiveData<List<SettingsItem>>
}