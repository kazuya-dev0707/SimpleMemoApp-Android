package com.example.simplememoapp_android.di

import android.app.Application
import androidx.room.Room
import com.example.simplememoapp_android.data.local.AppDatabase
import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.repository.MemoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "memo_database"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideMemoDao(db: AppDatabase): MemoDao {
        return db.memoDao()
    }

    @Provides
    @Singleton
    fun provideMemoRepository(dao: MemoDao): MemoRepository {
        return MemoRepository(dao)
    }
}