package com.example.simplememoapp_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplememoapp_android.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)

sealed interface AuthUiEvent {
    data class NavigateTo(val route: String) : AuthUiEvent
    data class ShowSnackbar(val message: String) : AuthUiEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AuthUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChange(password: String) { _uiState.update { it.copy(password = password) } }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.login(_uiState.value.email.trim(), _uiState.value.password.trim())
                .onSuccess {
                    _eventFlow.emit(AuthUiEvent.NavigateTo("memo_list"))
                }
                .onFailure { e ->
                    _eventFlow.emit(AuthUiEvent.ShowSnackbar(e.message ?: "ログインに失敗しました"))
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.register(_uiState.value.email.trim(), _uiState.value.password.trim())
                .onSuccess {
                    _eventFlow.emit(AuthUiEvent.NavigateTo("memo_list"))
                }
                .onFailure { e ->
                    _eventFlow.emit(AuthUiEvent.ShowSnackbar(e.message ?: "新規登録に失敗しました"))
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}