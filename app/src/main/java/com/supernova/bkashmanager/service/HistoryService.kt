package com.supernova.bkashmanager.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.supernova.bkashmanager.database.AppDatabase
import com.supernova.bkashmanager.model.ApiResponse
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.SharedPrefs
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryService: JobIntentService() {

    companion object {

        @JvmStatic
        fun enqueueWork(context: Context, id: Int, intent: Intent) {

            enqueueWork(context, HistoryService::class.java, id, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {

        Log.d("HistoryService", "Started")

        val prefs = SharedPrefs(applicationContext)
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiInterface.getHistories(prefs.adminEmail, prefs.adminToken)

        call?.enqueue(object: Callback<ApiResponse?> {

            override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {

                val body = response.body()

                if (body != null) {

                    if (body.status == Constants.STATUS_SUCCESS) {

                        val histories = body.histories

                        if (histories != null) {

                            insertHistories(histories)

                        } else {

                            Log.d("HistoryService::Error", "Histories Null")
                        }

                    } else {

                        Log.d("HistoryService::Error", body.failReason ?: "Null Body")
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {

                Log.d("HistoryService::Error", t.message ?: t.toString())
            }
        })
    }

    private fun insertHistories(histories: List<History>) {

        val dao = AppDatabase.getDatabase(applicationContext).appDao()

        GlobalScope.launch {

            dao.insertHistories(histories)
        }
    }
}