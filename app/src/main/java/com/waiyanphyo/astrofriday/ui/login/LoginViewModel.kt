package com.waiyanphyo.astrofriday.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

typealias Validator = (String) -> Boolean

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val emailValidator: Validator = { it.contains("@") && it.contains(".") }
    private val passwordValidator: Validator = { it.length > 7 }

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> get() = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> get() = _passwordError

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> get() = _isFormValid

    fun validate(email: String, password: String) {
        val emailIsValid = emailValidator(email)
        val passwordIsValid = passwordValidator(password)

        _emailError.value = if (emailIsValid) {
            null
        } else if (email.isEmpty()) {
            "Email is required."
        } else {
            "Invalid email address"
        }
        // If password is less than 8 characters, it would be an Incorrect Password
        _passwordError.value = if (passwordIsValid) {
            null
        } else if (password.isEmpty()) {
            "Password is required."
        } else {
            "Password Incorrect."
        }
        _isFormValid.value = emailIsValid && passwordIsValid
    }

}