package com.example.simplememoapp_android.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.simplememoapp_android.ui.viewmodel.SplashUiState
import com.example.simplememoapp_android.ui.viewmodel.SplashViewModel

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is SplashUiState.Authenticated -> {
                navController.navigate("memo_list") { popUpTo("splash") { inclusive = true } }
            }
            is SplashUiState.Unauthenticated -> {
                navController.navigate("login") { popUpTo("splash") { inclusive = true } }
            }
            is SplashUiState.Loading -> { /* Do Nothing */ }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}