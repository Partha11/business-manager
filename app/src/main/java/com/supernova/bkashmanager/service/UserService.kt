package com.supernova.bkashmanager.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.supernova.bkashmanager.database.AppDatabase
import com.supernova.bkashmanager.model.ApiResponse
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.SharedPrefs
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserService: JobIntentService() {

    companion object {

        @JvmStatic
        fun enqueueWork(context: Context, id: Int, intent: Intent) {

            enqueueWork(context, UserService::class.java, id, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {

        Log.d("UserService", "Started")

        val prefs = SharedPrefs(applicationContext)
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiInterface.getUsers(prefs.adminEmail, prefs.adminToken)

        call?.enqueue(object: Callback<ApiResponse?> {

            override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {

                val body = response.body()

                if (body != null) {

                    if (body.status == Constants.STATUS_SUCCESS) {

                        val users = body.users

                        if (users != null) {

                            insertUsers(users)
                        }

                    } else {

                        Log.d("UserService::Error", body.failReason ?: "Null Body")
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {

                Log.d("UserService::Error", t.message ?: t.toString())
            }
        })
    }

    private fun insertUsers(users: List<User>) {

        val dao = AppDatabase.getDatabase(applicationContext).appDao()

        GlobalScope.launch {

            dao.insertUsers(users)
        }
    }
}