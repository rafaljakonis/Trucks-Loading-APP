package com.example.trucksload.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trucksload.R
import com.example.trucksload.ui.login.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class LoginViewModel() : ViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.login_wrong_login_pattern)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.login_wrong_password_pattern)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.length in 5..20
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 5..20
    }

}