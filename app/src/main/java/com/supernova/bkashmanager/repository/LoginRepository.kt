package com.supernova.bkashmanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.supernova.bkashmanager.model.ApiResponse
import com.supernova.bkashmanager.service.ApiClient
import com.supernova.bkashmanager.service.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    private val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

    companion object {

        @JvmStatic
        private var instance: LoginRepository? = null

        @JvmStatic
        fun getInstance(): LoginRepository {

            if (instance == null) {

                instance = LoginRepository()
            }

            return instance!!
        }
    }

    fun authenticateUser(email: String, password: String): LiveData<ApiResponse> {

        val liveData = MutableLiveData<ApiResponse>()
        val call = apiInterface.authenticateUser(email, password)

        call?.enqueue(object: Callback<ApiResponse?> {

            override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {

                liveData.value = response.body()
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {

                val response = ApiResponse()

                response.status = "failed"
                response.failReason = t.localizedMessage

                liveData.value = response
            }
        })

        return liveData
    }
}