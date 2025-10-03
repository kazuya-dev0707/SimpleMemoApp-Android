package com.example.simplememoapp_android.ui.screen.parts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDetailTopAppBar(
    isSavable: Boolean,
    onNavigateBack: () -> Unit,
    onSaveClick: () -> Unit
) {
    TopAppBar(
        title = { Text("メモ") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
            }
        },
        actions = {
            IconButton(onClick = onSaveClick, enabled = isSavable) {
                Icon(Icons.Default.Done, contentDescription = "保存")
            }
        }
    )
}