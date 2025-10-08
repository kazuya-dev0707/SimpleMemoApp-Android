package com.example.simplememoapp_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.simplememoapp_android.ui.screen.LoginScreen
import com.example.simplememoapp_android.ui.screen.MemoDetailScreen
import com.example.simplememoapp_android.ui.screen.MemoListScreen
import com.example.simplememoapp_android.ui.screen.RegisterScreen
import com.example.simplememoapp_android.ui.screen.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") { // ★スタート地点を変更
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("memo_list") {
            MemoListScreen(navController = navController)
        }
        composable(
            route = "memo_detail/{memoId}",
            arguments = listOf(navArgument("memoId") { type = NavType.LongType })
        ) {
            MemoDetailScreen(navController = navController)
        }
    }
}