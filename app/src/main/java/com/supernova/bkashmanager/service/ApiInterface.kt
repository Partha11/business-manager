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

    @FormUrlEncoded
    @POST("update.php")
    fun updateUser(
            @Field("admin_email") email: String?,
            @Field("admin_token") token: String?,
            @Field("operation_type") type: Int,
            @Field("user_id") userId: Int = 0,
            @Field("point_amount") points: Int = 0
    ): Call<ApiResponse?>?

    @FormUrlEncoded
    @POST("history.php")
    fun updateTransaction(
            @Field("admin_email") email: String?,
            @Field("admin_token") token: String?,
            @Field("history_id") historyId: Int,
            @Field("trx_message") message: String?
    ): Call<ApiResponse?>?

    @POST("profile.php")
    @FormUrlEncoded
    fun updateProfile(
            @Field("admin_email") email: String?,
            @Field("admin_token") token: String?,
            @Field("update_type") type: Int,
            @Field("admin_name") name: String? = "",
            @Field("old_password") oldPassword: String? = "",
            @Field("new_password") newPassword: String? = "",
            @Field("wallet_points") points: Int = 0
    ): Call<ApiResponse?>?

    @POST("users.php")
    @FormUrlEncoded
    fun addNewUser(
        @Field("admin_email") email: String?,
        @Field("admin_token") token: String?,
        @Field("serialized_user") serializedUser: String
    ): Call<ApiResponse?>?
}