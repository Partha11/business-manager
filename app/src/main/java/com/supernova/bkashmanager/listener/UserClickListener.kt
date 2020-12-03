package com.supernova.bkashmanager.listener

import com.supernova.bkashmanager.model.User

interface UserClickListener {

    fun onUserClicked(user: User?)
}