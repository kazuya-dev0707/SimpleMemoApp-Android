package com.example.simplememoapp_android.di

import android.app.Application
import androidx.room.Room
import com.example.simplememoapp_android.data.local.AppDatabase
import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.repository.MemoRepository
import com.example.simplememoapp_android.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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

    // ★★★ 以下を追記 ★★★
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            // ▼▼▼ Step1で控えたあなたのURLに書き換える！ ▼▼▼
            .baseUrl("https://68e49a478e116898997c3093.mockapi.io/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}