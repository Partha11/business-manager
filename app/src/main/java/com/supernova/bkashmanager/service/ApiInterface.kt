package com.supernova.bkashmanager.service

import com.supernova.bkashmanager.model.ApiResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    fun authenticateUser(
        @Field("admin_email") email: String?,
        @Field("admin_password") password: String?
    ): Call<ApiResponse?>?

    @GET("profile.php")
    fun getAdminProfile(
        @Query("admin_email") email: String?,
        @Query("admin_token") token: String?
    ): Call<ApiResponse?>?

    @GET("users.php")
    fun getUsers(
            @Query("admin_email") email: String?,
            @Query("admin_token") token: String?
    ): Call<ApiResponse?>?

    @GET("history.php")
    fun getHistories(
            @Query("admin_email") email: String?,
            @Query("admin_token") token: String?
    ): Call<ApiResponse?>?
}