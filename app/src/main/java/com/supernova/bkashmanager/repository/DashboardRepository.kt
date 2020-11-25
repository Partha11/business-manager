package com.supernova.bkashmanager.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.supernova.bkashmanager.database.AppDao
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.User

class DashboardRepository(private val dao: AppDao) {

    companion object {

        @JvmStatic
        private var instance: DashboardRepository? = null

        @JvmStatic
        fun getInstance(dao: AppDao): DashboardRepository {

            if (instance == null) {

                instance = DashboardRepository(dao)
            }

            return instance!!
        }
    }

    fun getUsers(): LiveData<List<User>> {

        return dao.getUsers()
    }

    fun getHistories(date: String): LiveData<List<History>> {

        Log.d("Date", date)
        return dao.getHistories(date)
    }
}