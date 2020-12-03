package com.supernova.bkashmanager.util

import com.supernova.bkashmanager.BuildConfig

object Constants {

    const val PREF_NAME = BuildConfig.APPLICATION_ID + ".data"
    const val PREF_ADMIN_NAME = "admin_name"
    const val PREF_ADMIN_EMAIL = "admin_email"
    const val PREF_ADMIN_TOKEN = "admin_token"
    const val PREF_REMEMBER_USER = "remember_user"
    const val PREF_LOGGED_IN = "logged_in"
    const val PREF_INITIAL_POINTS = "initial_points"
    const val PREF_CURRENT_POINTS = "current_points"

    const val DATABASE_NAME = BuildConfig.APPLICATION_ID + ".userdb"
    const val DATABASE_VERSION = 1

    const val TABLE_USERS = "users"
    const val TABLE_USER_ID = "id"
    const val TABLE_USER_NAME = "user_name"
    const val TABLE_USER_EMAIL = "user_email"
    const val TABLE_USER_NUMBER = "user_number"
    const val TABLE_USER_STATUS = "is_banned"
    const val TABLE_USER_TOTAL_POINTS = "total_points"
    const val TABLE_USER_CURRENT_POINTS = "current_points"

    const val TABLE_HISTORY = "histories"
    const val TABLE_HISTORY_ID = "id"
    const val TABLE_HISTORY_PHONE_NUMBER = "phone_number"
    const val TABLE_HISTORY_TRX_AMOUNT = "trx_amount"
    const val TABLE_HISTORY_TRX_TYPE = "trx_type"
    const val TABLE_HISTORY_TRX_TYPE_TEXT = "trx_type_text"
    const val TABLE_HISTORY_TRX_MEDIUM = "trx_medium"
    const val TABLE_HISTORY_TRX_MEDIUM_TEXT = "trx_medium_text"
    const val TABLE_HISTORY_USER_ID = "user_id"
    const val TABLE_HISTORY_IS_COMPLETED = "is_completed"
    const val TABLE_HISTORY_REQUEST_TIME = "request_time"
    const val TABLE_HISTORY_COMPLETION_TIME = "completion_time"

    const val WEB_URL = "https://supernova-dev.xyz/bkash_manager/api/"

    const val STATUS_SUCCESS = "success"
    const val STATUS_FAILED = "failed"

    const val DATA_USER_TOKEN = "user_token"
    const val DATA_USER_EMAIL = "user_email"

    const val DIALOG_TYPE_TEXT = 0
    const val DIALOG_TYPE_PASSWORD = 1
    const val DIALOG_TYPE_NUMBER = 2

    const val TYPE_NAME = 0
    const val TYPE_PASSWORD = 1
    const val TYPE_POINTS = 2

    const val JOB_FETCH_PROFILE = 100
    const val JOB_FETCH_USERS = 101
    const val JOB_FETCH_HISTORIES = 102

    const val SETTINGS_ITEM_BASE = 1000
    const val SETTINGS_ITEM_NAME = 1000
    const val SETTINGS_ITEM_PASSWORD = 1001

    const val VIEW_SECTION_TITLE = 1
    const val VIEW_SECTION_ITEM = 2

    const val USER_BLOCK = 0

    const val LIST_TYPE_USER = 1
    const val LIST_TYPE_HISTORY = 2
}