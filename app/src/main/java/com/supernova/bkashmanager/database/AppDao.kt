package com.supernova.bkashmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.model.UserHistory
import com.supernova.bkashmanager.util.Constants

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistories(users: List<History>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateHistory(history: History)

    @Query("SELECT * FROM ${Constants.TABLE_USERS} ORDER BY ${Constants.TABLE_USER_ID} DESC")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM ${Constants.TABLE_USERS} WHERE ${Constants.TABLE_USER_ID} = :id")
    fun getUser(id: Int): LiveData<User>

//    @Query("SELECT h.*, ${Constants.TABLE_USER_NAME} FROM ${Constants.TABLE_HISTORY} h LEFT JOIN " +
//            "(SELECT ${Constants.TABLE_USER_ID} AS uid, ${Constants.TABLE_USER_NAME} FROM ${Constants.TABLE_USERS}) u " +
//            "ON h.${Constants.TABLE_HISTORY_USER_ID} = u.uid WHERE DATE(${Constants.TABLE_HISTORY_REQUEST_TIME}) = :date " +
//            "ORDER BY ${Constants.TABLE_HISTORY_REQUEST_TIME} DESC")
//    fun getHistories(date: String): LiveData<List<History>>

    @Transaction
    @Query("SELECT * FROM ${Constants.TABLE_HISTORY} WHERE DATE(${Constants.TABLE_HISTORY_REQUEST_TIME}) = :date " +
            "ORDER BY ${Constants.TABLE_HISTORY_REQUEST_TIME} DESC")
    fun getHistories(date: String): LiveData<List<UserHistory>>
}