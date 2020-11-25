package com.supernova.bkashmanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.util.Constants

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistories(users: List<History>)

    @Query("SELECT * FROM ${Constants.TABLE_USERS}")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM ${Constants.TABLE_HISTORY} WHERE DATE(${Constants.TABLE_HISTORY_REQUEST_TIME}) = :date ORDER BY ${Constants.TABLE_HISTORY_REQUEST_TIME} DESC")
    fun getHistories(date: String): LiveData<List<History>>
}