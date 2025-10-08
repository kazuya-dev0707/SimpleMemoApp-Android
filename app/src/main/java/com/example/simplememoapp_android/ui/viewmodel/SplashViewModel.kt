package com.example.simplememoapp_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplememoapp_android.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface SplashUiState {
    object Loading : SplashUiState
    data class Authenticated(val userId: String) : SplashUiState
    object Unauthenticated : SplashUiState
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    val uiState: StateFlow<SplashUiState> = userRepository.loggedInUserIdFlow
        .map { userId ->
            if (userId.isNullOrBlank()) {
                SplashUiState.Unauthenticated
            } else {
                SplashUiState.Authenticated(userId)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SplashUiState.Loading
        )
}