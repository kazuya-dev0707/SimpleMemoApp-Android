package com.example.simplememoapp_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.model.Memo

@Database(entities = [Memo::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao

    companion object {
        // ★ バージョン1から2へのMigrationを定義
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // memosテーブルにtitleとupdatedAtカラムを追加するSQL
                database.execSQL("ALTER TABLE memos ADD COLUMN title TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE memos ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}