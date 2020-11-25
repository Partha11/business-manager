package com.supernova.bkashmanager.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.supernova.bkashmanager.database.AppDatabase
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.repository.DashboardRepository
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.SharedPrefs
import com.supernova.bkashmanager.util.Utils

class DashboardViewModel(application: Application): AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).appDao()
    private val repository = DashboardRepository.getInstance(dao)

    fun createStorageListener(): LiveData<Pair<String, String>> {

        val callback = MutableLiveData<Pair<String, String>>()
        val prefs = SharedPrefs(getApplication())

        prefs.prefs.registerOnSharedPreferenceChangeListener { _, key ->

            Log.d("ViewModel", "Storage Update: $key")

            when (key) {

                Constants.PREF_ADMIN_NAME -> callback.value = Pair(key, prefs.adminName)
                Constants.PREF_INITIAL_POINTS -> callback.value = Pair(key, prefs.initialPoints.toString())
                Constants.PREF_CURRENT_POINTS -> callback.value = Pair(key, prefs.currentPoints.toString())
            }
        }

        return callback
    }

    fun getUsers(): LiveData<List<User>> {

        return repository.getUsers()
    }

    fun getHistories(date: String): LiveData<List<History>> {

        return repository.getHistories(if (date.isEmpty()) Utils.getCurrentDate() else date)
    }
}