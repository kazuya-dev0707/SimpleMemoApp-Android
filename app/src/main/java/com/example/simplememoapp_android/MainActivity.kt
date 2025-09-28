package com.example.simplememoapp_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.simplememoapp_android.ui.screen.MemoScreen
import com.example.simplememoapp_android.ui.theme.SimpleMemoAppAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleMemoAppAndroidTheme {
                // Surfaceでアプリの背景を設定する
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 主役である「MemoScreen」をここに配置！
                    MemoScreen()
                }
            }
        }
    }
}