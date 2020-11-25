package com.supernova.bkashmanager.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.supernova.bkashmanager.model.ApiResponse
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.SharedPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataService: JobIntentService() {

    companion object {

        @JvmStatic
        fun enqueueWork(context: Context, id: Int, intent: Intent) {

            enqueueWork(context, DataService::class.java, id, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {

        Log.d("DataService", "Started")

        val prefs = SharedPrefs(applicationContext)
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiInterface.getAdminProfile(prefs.adminEmail, prefs.adminToken)

        call?.enqueue(object: Callback<ApiResponse?> {

            override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {

                val body = response.body()

                if (body != null) {

                    if (body.status == Constants.STATUS_SUCCESS) {

                        val user = body.user

                        prefs.adminName = user?.userName ?: ""
                        prefs.currentPoints = user?.currentPoints ?: 0
                        prefs.initialPoints = user?.initialPoints ?: 0

                        Log.d("DataService::Update", "success")

                    } else {

                        Log.d("DataService::Error", body.failReason ?: "Null Body")
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {

                Log.d("DataService::Error", t.message ?: t.toString())
            }
        })
    }
}