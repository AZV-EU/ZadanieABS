package dev.azv.zadanieabs.domain.model.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.azv.zadanieabs.R
import dev.azv.zadanieabs.common.Constants
import dev.azv.zadanieabs.data.user.SessionManager
import dev.azv.zadanieabs.data.user.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
): ViewModel() {
    private val _usernameError = MutableStateFlow<Int?>(null)
    val usernameError: StateFlow<Int?> = _usernameError

    private val _passwordError = MutableStateFlow<Int?>(null)
    val passwordError: StateFlow<Int?> = _passwordError

    private val _loginResult = MutableStateFlow<Int?>(null)
    val loginResult: StateFlow<Int?> = _loginResult

    private val _isDataValid = MutableStateFlow(false)
    val isDataValid: StateFlow<Boolean> = _isDataValid

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy

    var loginJob: Job? = null

    private fun cleanup() {
        _usernameError.value = null
        _passwordError.value = null
        _loginResult.value = null
    }

    fun checkData(username: String, password: String) {
        cleanup()

        if (!isUsernameValid(username))
            _usernameError.value = R.string.invalid_username

        if (!isPasswordValid(password))
            _passwordError.value = R.string.invalid_password

        _isDataValid.value = usernameError.value == null && passwordError.value == null
    }

    fun tryLogin(username: String, password: String): Boolean {
        if (sessionManager.isLoggedIn) return true
        if (loginJob?.isActive == true) return false
        cleanup()
        _isBusy.value = true

        loginJob = viewModelScope.launch {
            userRepository.tryLogin(username, password).collect { loginResult ->
                sessionManager.setUser(loginResult.userData)
                _loginResult.value = loginResult.errorMessage
            }
            _isBusy.value = false
        }
        return sessionManager.isLoggedIn
    }

    private fun isUsernameValid(username: String): Boolean = username.length >= 3
    private fun isPasswordValid(password: String): Boolean = password.length > 5
}