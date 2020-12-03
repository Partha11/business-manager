package com.supernova.bkashmanager.model

import com.google.gson.annotations.SerializedName

class ApiResponse {

    @SerializedName("status")
    var status: String? = ""

    @SerializedName("fail_reason")
    var failReason: String? = ""

    @SerializedName("admin_token")
    var adminToken: String? = ""

    @SerializedName("admin_name")
    var adminName: String? = ""

    @SerializedName("user")
    var user: User? = null

    @SerializedName("history")
    var history: History? = null

    @SerializedName("users")
    var users: List<User>? = null

    @SerializedName("histories")
    var histories: List<History>? = null
}