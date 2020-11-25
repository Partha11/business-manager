package com.supernova.bkashmanager.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.supernova.bkashmanager.util.Constants

@Keep
@Entity(tableName = Constants.TABLE_HISTORY)
class History {

    @PrimaryKey
    @ColumnInfo(name = Constants.TABLE_HISTORY_ID)
    @SerializedName("id")
    var id: Int = 0

    @ColumnInfo(name = Constants.TABLE_HISTORY_PHONE_NUMBER)
    @SerializedName("phone_number")
    var phoneNumber : String? = ""

    @ColumnInfo(name = Constants.TABLE_HISTORY_TRX_AMOUNT)
    @SerializedName("transaction_amount")
    var transactionAmount : Int = 0

    @ColumnInfo(name = Constants.TABLE_HISTORY_TRX_TYPE)
    @SerializedName("transaction_type")
    var transactionType : Int = 0

    @ColumnInfo(name = Constants.TABLE_HISTORY_TRX_TYPE_TEXT)
    @SerializedName("transaction_type_text")
    var transactionTypeText : String? = ""

    @ColumnInfo(name = Constants.TABLE_HISTORY_TRX_MEDIUM)
    @SerializedName("transaction_medium")
    var transactionMedium : Int = 0

    @ColumnInfo(name = Constants.TABLE_HISTORY_TRX_MEDIUM_TEXT)
    @SerializedName("transaction_medium_text")
    var transactionMediumText : String? = ""

    @ColumnInfo(name = Constants.TABLE_HISTORY_USER_ID)
    @SerializedName("user_id")
    var userId : Int = 0

    @ColumnInfo(name = Constants.TABLE_HISTORY_IS_COMPLETED)
    @SerializedName("is_completed")
    var completionFlag : Int = 0

    @ColumnInfo(name = Constants.TABLE_HISTORY_REQUEST_TIME)
    @SerializedName("request_time")
    var requestTime : String? = ""

    @ColumnInfo(name = Constants.TABLE_HISTORY_COMPLETION_TIME)
    @SerializedName("completion_time")
    var completionTime : String? = ""
}