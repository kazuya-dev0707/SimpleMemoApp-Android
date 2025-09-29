package com.example.simplememoapp_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.model.Memo

@Database(entities = [Memo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}