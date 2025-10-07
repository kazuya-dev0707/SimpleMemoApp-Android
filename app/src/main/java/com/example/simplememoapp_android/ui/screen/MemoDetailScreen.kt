package com.example.simplememoapp_android.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.simplememoapp_android.MemoApplication
import com.example.simplememoapp_android.ui.screen.parts.MemoDetailContent
import com.example.simplememoapp_android.ui.screen.parts.MemoDetailTopAppBar
import com.example.simplememoapp_android.ui.viewmodel.MemoDetailViewModel
import com.example.simplememoapp_android.ui.viewmodel.MemoListViewModel
import com.example.simplememoapp_android.ui.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MemoDetailScreen(
    navController: NavController,
    // ★ ここから backStackEntry が削除されている
) {
    // ★★★ ViewModelの取得が、この一行だけで完了！ ★★★
    val viewModel: MemoDetailViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            navController.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            MemoDetailTopAppBar(
                isSavable = uiState.isSavable,
                onNavigateBack = { navController.popBackStack() },
                onSaveClick = { viewModel.saveMemo() }
            )
        }
    ) { paddingValues ->
        MemoDetailContent(
            title = uiState.title,
            content = uiState.content,
            onTitleChange = { viewModel.onTitleChange(it) },
            onContentChange = { viewModel.onContentChange(it) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}