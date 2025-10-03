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
                    // ★★★ ここから変更 ★★★
                    // 1. ナビゲーションの司令塔(NavController)を作成
                    val navController = rememberNavController()
                    // 2. AppNavHostを呼び出し、司令塔を渡す
                    AppNavHost(navController = navController)
                    // ★★★ ここまで変更 ★★★
                }
            }
        }
    }
}

//TODO AppNavHostはMainActivityの外に定義する方が一般的ですが、中でも問題ありません。
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "memo_list") {
        composable("memo_list") {
            // MemoListScreenにnavControllerを渡す
            MemoListScreen(navController = navController)
        }
        composable(
            route = "memo_detail/{memoId}",
            arguments = listOf(navArgument("memoId") { type = NavType.LongType })
        ) { backStackEntry ->
            // MemoDetailScreenにnavControllerを渡す
            MemoDetailScreen(navController = navController)
        }
    }
}