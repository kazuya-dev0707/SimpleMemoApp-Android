package com.example.simplememoapp_android

import android.app.Application
import androidx.room.Room
import com.example.simplememoapp_android.data.local.AppDatabase
import com.example.simplememoapp_android.data.local.AppDatabase.Companion.MIGRATION_1_2
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
            .addMigrations(MIGRATION_1_2)
            // ★★★マイグレーションに失敗した場合、データベースを破棄して再生成する★★★
            .fallbackToDestructiveMigration()
            .build()
    }

    // アプリ全体で共有されるMemoRepositoryインスタンス
    val repository: MemoRepository by lazy {
        MemoRepository(database.memoDao())
    }
}