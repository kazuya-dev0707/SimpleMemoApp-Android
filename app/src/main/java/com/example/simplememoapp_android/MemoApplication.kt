package com.example.simplememoapp_android

import android.app.Application
import androidx.room.Room
import com.example.simplememoapp_android.data.local.AppDatabase
import com.example.simplememoapp_android.data.local.Converters
import com.example.simplememoapp_android.data.repository.MemoRepository

class MemoApplication : Application() {
    // アプリ全体で共有されるAppDatabaseインスタンス
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "memo_database"
        )
            // TypeConverterはRoom 2.6.0以降、自動適用されるため明示的な追加は不要な場合がある
            // .addTypeConverter(Converters::class.java)
            .build()
    }

    // アプリ全体で共有されるMemoRepositoryインスタンス
    val repository: MemoRepository by lazy {
        MemoRepository(database.memoDao())
    }
}