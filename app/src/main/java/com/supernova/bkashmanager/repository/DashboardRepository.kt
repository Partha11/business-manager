package com.supernova.bkashmanager.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.supernova.bkashmanager.database.AppDao
import com.supernova.bkashmanager.model.ApiResponse
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.model.UserHistory
import com.supernova.bkashmanager.service.ApiClient
import com.supernova.bkashmanager.service.ApiInterface
import com.supernova.bkashmanager.util.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepository(private val dao: AppDao) {

    private val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

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

    fun getUser(id: Int): LiveData<User> {

        return dao.getUser(id)
    }

    fun getHistories(date: String): LiveData<List<UserHistory>> {

        return dao.getHistories(date)
    }

    fun updateUser(email: String, token: String, operation: Int, userId: Int, points: Int): LiveData<ApiResponse> {

        val callback = MutableLiveData<ApiResponse>()
        val call = apiInterface.updateUser(email, token, operation, userId, points)

        if (operation == Constants.TYPE_USER_POINTS && points == Constants.INVALID_POINT) {

            val response = ApiResponse()

            response.status = "failed"
            response.failReason = "Invalid point amount set. Please check point amount and try again."

            callback.value = response

        } else {

            call?.enqueue(object: Callback<ApiResponse?> {

                override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {

                    val body = response.body()

                    if (body != null && body.status == Constants.STATUS_SUCCESS) {

                        val user = body.user

                        GlobalScope.launch {

                            if (user != null) {

                                dao.updateUser(user)
                            }
                        }
                    }

                    callback.value = body
                }

                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {

                    val response = ApiResponse()

                    response.status = "failed"
                    response.failReason = t.message.toString()

                    callback.value = response
                }
            })
        }

        return callback
    }

    fun updateHistory(email: String, token: String, historyId: Int, message: String?): LiveData<ApiResponse> {

        val callback = MutableLiveData<ApiResponse>()
        val call = apiInterface.updateTransaction(email, token, historyId, message)

        call?.enqueue(object: Callback<ApiResponse?> {

            override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {

                val body = response.body()

                if (body != null && body.status == Constants.STATUS_SUCCESS) {

                    val history = body.history

                    GlobalScope.launch {

                        if (history != null) {

                            dao.updateHistory(history)
                        }
                    }
                }

                callback.value = body
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {

                val response = ApiResponse()

                response.status = "failed"
                response.failReason = t.message.toString()

                callback.value = response
            }
        })

        return callback
    }

    fun updateProfile(email: String, token: String, type: Int, text: String?, text2: String?): LiveData<ApiResponse> {

        val callback = MutableLiveData<ApiResponse>()
        var number = 0

        try {

            if (text != null) {

                number = text.toInt()
            }

        } catch (ex: Exception) {

            number = -1
        }

        if (type == Constants.TYPE_POINTS && number == -1) {

            val response = ApiResponse()

            response.status = "failed"
            response.failReason = "Invalid point amount. Please check your point amount and try again."

            callback.value = response

        } else {

            val call = when (type) {

                Constants.TYPE_NAME -> apiInterface.updateProfile(email, token, type, text)
                Constants.TYPE_PASSWORD -> apiInterface.updateProfile(email, token, type, "", text, text2)
                else -> apiInterface.updateProfile(email, token, type, "", "", "", number)
            }

            call?.enqueue(object: Callback<ApiResponse?> {

                override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {

                    callback.value = response.body()
                }

                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {

                    val response = ApiResponse()

                    response.status = "failed"
                    response.failReason = t.message.toString()

                    callback.value = response
                }
            })
        }

        return callback
    }

    fun addNewUser(email: String, token: String, serializedUser: String): LiveData<ApiResponse> {

        val callback = MutableLiveData<ApiResponse>()
        val call = apiInterface.addNewUser(email, token, serializedUser)

        call?.enqueue(object: Callback<ApiResponse?> {

            override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {

                val body = response.body()
                Log.d("Resp", Gson().toJson(body))

                if (body != null && body.status == Constants.STATUS_SUCCESS) {

                    val user = body.user

                    GlobalScope.launch {

                        if (user != null) {

                            dao.insertUser(user)
                        }
                    }
                }

                callback.value = body
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {

                val response = ApiResponse()

                response.status = "failed"
                response.failReason = t.message.toString()

                callback.value = response
            }
        })

        return callback
    }
}