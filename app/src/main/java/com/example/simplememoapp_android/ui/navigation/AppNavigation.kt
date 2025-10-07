package com.example.simplememoapp_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.simplememoapp_android.ui.screen.MemoDetailScreen
import com.example.simplememoapp_android.ui.screen.MemoListScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "memo_list") {
        composable("memo_list") {
            MemoListScreen(navController = navController)
        }
        composable(
            route = "memo_detail/{memoId}",
            arguments = listOf(navArgument("memoId") { type = NavType.LongType })
        ) { // ★ backStackEntry を受け取る必要がなくなる
            // ★ backStackEntry を渡す必要がなくなり、呼び出しがシンプルになる
            MemoDetailScreen(navController = navController)
        }
    }
}