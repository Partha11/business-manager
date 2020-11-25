package com.supernova.bkashmanager.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.supernova.bkashmanager.model.ApiResponse
import com.supernova.bkashmanager.repository.LoginRepository

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private var repository = LoginRepository.getInstance()

    fun authenticateUser(email: String, password: String): LiveData<ApiResponse> {

        return repository.authenticateUser(email, password)
    }

    fun validateEmail(email: String): Boolean {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}