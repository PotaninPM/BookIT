package com.prod.bookit.presentation.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prod.bookit.domain.repository.AuthRepository
import com.prod.bookit.presentation.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "AuthViewModel"

class AuthViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthorized)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            handleAuthAction {
                authRepository.login(email, password)
            }
        }
    }

    fun register(email: String, password: String, fullName: String, avatarUri: Uri?, isAdmin: Boolean) {
        viewModelScope.launch {
            handleAuthAction {
                authRepository.register(email, password, fullName, avatarUri, isAdmin)
            }
        }
    }

    fun signInWithYandex(token: String) {
        viewModelScope.launch {
            handleAuthAction {
                authRepository.signInWithYandex(token)
            }
        }
    }

    private suspend fun handleAuthAction(action: suspend () -> Boolean) {
        _authState.value = AuthState.Loading
        
        try {
            val success = action()
            _authState.value = if (success) {
                AuthState.Authorized
            } else {
                AuthState.Error("Authentication failed")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Authentication error", e)
            _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
        }
    }

    fun logout() {
        authRepository.clearToken()
        _authState.value = AuthState.Unauthorized
    }
}