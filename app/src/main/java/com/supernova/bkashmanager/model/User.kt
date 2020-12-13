package com.supernova.bkashmanager.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.supernova.bkashmanager.util.Constants

@Keep
@Entity(tableName = Constants.TABLE_USERS)
class User {

    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = Constants.TABLE_USER_ID)
    var userId: Int = 0

    @SerializedName("user_email")
    @ColumnInfo(name = Constants.TABLE_USER_EMAIL)
    var userEmail: String? = ""

    @SerializedName("user_name")
    @ColumnInfo(name = Constants.TABLE_USER_NAME)
    var userName: String? = ""

    @SerializedName("user_token")
    @Ignore
    var userToken: String? = ""

    @SerializedName("user_password")
    @Ignore
    var userPassword: String? = ""

    @SerializedName("user_number")
    @ColumnInfo(name = Constants.TABLE_USER_NUMBER)
    var userNumber: String? = ""

    @SerializedName("is_banned")
    @ColumnInfo(name = Constants.TABLE_USER_STATUS)
    var banStatus: Int = 0

    @SerializedName("initial_points")
    @Ignore
    var initialPoints: Int = 0

    @SerializedName("current_points")
    @ColumnInfo(name = Constants.TABLE_USER_CURRENT_POINTS)
    var currentPoints: Int = 0

    @SerializedName("total_points")
    @ColumnInfo(name = Constants.TABLE_USER_TOTAL_POINTS)
    var totalPoints: Int = 0
}