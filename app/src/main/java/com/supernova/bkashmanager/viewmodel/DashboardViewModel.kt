package com.supernova.bkashmanager.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.supernova.bkashmanager.database.AppDatabase
import com.supernova.bkashmanager.model.ApiResponse
import com.supernova.bkashmanager.model.SettingsItem
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.model.UserHistory
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

    fun getUser(id: Int): LiveData<User> {

        return repository.getUser(id)
    }

    fun getHistories(date: String): LiveData<List<UserHistory>> {

        return repository.getHistories(if (date.isEmpty()) Utils.getCurrentDate() else date)
    }

    fun parseSettingsItems(context: Context): LiveData<List<SettingsItem>> {

        val liveData = MutableLiveData<List<SettingsItem>>()
        val settingsItems = mutableListOf<SettingsItem>()
        val fileName = "settings.json"
        val file: String = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val type = object: TypeToken<List<SettingsItem>>() { }.type
        val data: List<SettingsItem> = Gson().fromJson(file, type)

        for (item in data) {

            if (item.viewType == Constants.VIEW_SECTION_ITEM) {

                if (item.thumbIconString.isNotEmpty()) {

                    val image = "@drawable/" + item.thumbIconString
                    val resource = context.resources.getIdentifier(image, null, context.packageName)
                    val drawable = ContextCompat.getDrawable(context, resource)

                    item.thumbIcon = drawable
                }
            }

            settingsItems.add(item)
        }

        liveData.value = settingsItems
        return liveData
    }

    fun updateUser(email: String, token: String, operation: Int, userId: Int, points: String? = ""): LiveData<ApiResponse> {

        var pointInt = -1

        try {

            if (points != null) {

                pointInt = points.toInt()
            }

        } catch (ex: Exception) {

            ex.printStackTrace()
        }

        return repository.updateUser(email, token, operation, userId, pointInt)
    }

    fun updateHistory(email: String, token: String, id: Int, message: String?): LiveData<ApiResponse> {

        return repository.updateHistory(email, token, id, message)
    }

    fun updateProfile(email: String, token: String, type: Int, text: String?, text2: String?): LiveData<ApiResponse> {

        return repository.updateProfile(email, token, type, text, text2)
    }

    fun addNewUser(email: String, token: String, user: User): LiveData<ApiResponse> {

        return repository.addNewUser(email, token, Gson().toJson(user))
    }
}