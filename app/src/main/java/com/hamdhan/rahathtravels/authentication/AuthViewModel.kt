package com.hamdhan.rahathtravels.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    // This holds the result of login/register actions
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all fields")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.LoginSuccess
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Invalid email or password")
            }
        }
    }

    fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all fields")
            return
        }

        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.RegisterSuccess
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun forgotPassword(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Please enter your email")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                _authState.value = AuthState.ForgotPasswordSuccess
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Email not found")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

// All possible states
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object LoginSuccess : AuthState()
    object RegisterSuccess : AuthState()
    object ForgotPasswordSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}