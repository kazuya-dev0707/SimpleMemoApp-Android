package com.example.simplememoapp_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simplememoapp_android.ui.screen.MemoDetailScreen
import com.example.simplememoapp_android.ui.screen.MemoListScreen
import com.example.simplememoapp_android.ui.theme.SimpleMemoAppAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleMemoAppAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "memo_list") {
        composable("memo_list") {
            MemoListScreen(navController = navController)
        }
        composable(
            route = "memo_detail/{memoId}",
            arguments = listOf(navArgument("memoId") { type = NavType.LongType })
        ) { backStackEntry ->
            // ★★★ 変更点: backStackEntryをMemoDetailScreenに渡す ★★★
            MemoDetailScreen(
                navController = navController,
                backStackEntry = backStackEntry
            )
        }
    }
}