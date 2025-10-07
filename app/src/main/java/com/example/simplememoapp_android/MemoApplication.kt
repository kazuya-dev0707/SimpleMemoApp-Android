package com.example.simplememoapp_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MemoApplication : Application() {
    // 中身は空っぽでOK！
    // Hiltがすべてを裏側で管理してくれます。
}