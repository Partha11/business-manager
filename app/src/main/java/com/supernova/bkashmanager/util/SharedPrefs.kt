package com.supernova.bkashmanager.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {

    val prefs: SharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor? = null

    var adminName: String

        get() = prefs.getString(Constants.PREF_ADMIN_NAME, "Mr Manager") ?: ""

        set(name) {

            editor = prefs.edit()

            editor?.putString(Constants.PREF_ADMIN_NAME, name)
            editor?.apply()
        }

    var adminEmail: String

        get() = prefs.getString(Constants.PREF_ADMIN_EMAIL, "") ?: ""

        set(email) {

            editor = prefs.edit()

            editor?.putString(Constants.PREF_ADMIN_EMAIL, email)
            editor?.apply()
        }

    var adminToken: String

        get() = prefs.getString(Constants.PREF_ADMIN_TOKEN, "") ?: ""

        set(token) {

            editor = prefs.edit()

            editor?.putString(Constants.PREF_ADMIN_TOKEN, token)
            editor?.apply()
        }

    var rememberUser: Boolean

        get() = prefs.getBoolean(Constants.PREF_REMEMBER_USER, true)

        set(value) {

            editor = prefs.edit()

            editor?.putBoolean(Constants.PREF_REMEMBER_USER, value)
            editor?.apply()
        }

    var hasLoggedIn: Boolean

        get() = prefs.getBoolean(Constants.PREF_LOGGED_IN, false)

        set(value) {

            editor = prefs.edit()

            editor?.putBoolean(Constants.PREF_LOGGED_IN, value)
            editor?.apply()
        }

    var initialPoints: Int

        get() = prefs.getInt(Constants.PREF_INITIAL_POINTS, 0)

        set(points) {

            editor = prefs.edit()

            editor?.putInt(Constants.PREF_INITIAL_POINTS, points)
            editor?.apply()
        }

    var currentPoints: Int

        get() = prefs.getInt(Constants.PREF_CURRENT_POINTS, 0)

        set(points) {

            editor = prefs.edit()

            editor?.putInt(Constants.PREF_CURRENT_POINTS, points)
            editor?.apply()
        }
}