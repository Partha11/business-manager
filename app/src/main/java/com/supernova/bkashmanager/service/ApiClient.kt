package com.supernova.bkashmanager.service

import com.supernova.bkashmanager.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {

        @JvmStatic
        fun getClient(): Retrofit {

            return Retrofit.Builder()
                .baseUrl(Constants.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}