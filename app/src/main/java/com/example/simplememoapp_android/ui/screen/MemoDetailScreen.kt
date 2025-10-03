package com.example.simplememoapp_android.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
// ★ navControllerを引数に追加
fun MemoDetailScreen(navController: NavController) {
    Text("詳細画面")
}

@Preview(showBackground = true)
@Composable
fun MemoDetailScreenPreview() {
    // Preview用にダミーのNavControllerを渡す
    MemoDetailScreen(navController = rememberNavController())
}